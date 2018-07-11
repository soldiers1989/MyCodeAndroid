package com.dm.utils;

import java.util.ArrayDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池
 * @author jiaohongyun
 */
public class ThreadsPool
{
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	
	private static int threadCount = CPU_COUNT * 2 + 3;// 线程池数量
	
	private static final ThreadFactory sThreadFactory = new ThreadFactory()
	{
		private final AtomicInteger mCount = new AtomicInteger(1);
		
		@Override
		public Thread newThread(Runnable r)
		{
			Thread tread = new Thread(r, "Dimeng theads #" + mCount.getAndIncrement());
			// 设置线程优先级
			tread.setPriority(Thread.NORM_PRIORITY - 1);
			return tread;
		}
	};
	
	public static final Executor SERIAL_EXECUTOR = new SerialExecutor();
	
	public static final Executor THREAD_POOL_EXECUTOR = Executors.newFixedThreadPool(threadCount, sThreadFactory);
	
	/**
	 * 线程管理
	 */
	//	private static final Executor executor = Executors.newFixedThreadPool(threadCount, sThreadFactory);
	
	/**
	 * 执行一个任务
	 * @param runnable
	 * @see [类、类#方法、类#成员]
	 */
	public static void executeOnExecutor(Runnable runnable)
	{
		THREAD_POOL_EXECUTOR.execute(runnable);
	}
	
	public static Executor getExecutor()
	{
		return THREAD_POOL_EXECUTOR;
	}
	
	private static class SerialExecutor implements Executor
	{
		final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
		
		Runnable mActive;
		
		public synchronized void execute(final Runnable r)
		{
			mTasks.offer(new Runnable()
			{
				public void run()
				{
					try
					{
						r.run();
					}
					finally
					{
						scheduleNext();
					}
				}
			});
			if (mActive == null)
			{
				scheduleNext();
			}
		}
		
		protected synchronized void scheduleNext()
		{
			if ((mActive = mTasks.poll()) != null)
			{
				THREAD_POOL_EXECUTOR.execute(mActive);
			}
		}
	}
}
