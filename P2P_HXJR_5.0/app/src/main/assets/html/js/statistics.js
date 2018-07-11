var charWidth = screen.width*0.8;

function refresh0(data){
	//第0页面数据刷新
	$('#totalInvestAmount').html(Number(data.totalInvestAmount).toFixed(2)+'元');
	$('#totalRegister').html(data.totalRegister+'人');
	$('#totalEarnAmount').html(Number(data.totalEarnAmount).toFixed(2)+'元');
	$('#tradeCount').html(data.tradeCount+'笔');
}
function refresh1(_data){
			//第1页面数据刷新拆线图
	$('#container1').highcharts({
		    chart: {
				type: 'line',
				margin: [50,20,100,80]//上右下左
			},
			title: {
				text: '<div class="page-title">半年累计投资金额走势</div>',
				useHTML:true
			},
			subtitle: {
				text: '',
				x: -20
			},
			xAxis: {
				categories: _data.times
			},
			yAxis: {
				title: {
					text: '金额(元)'
				},

                labels:{
                formatter:function(){
                //return this.value/10000+'万';
                 if (this.value >= 100000000){
                        return this.value / 100000000 +'亿';
                    }else if(this.value>= 10000000){
                        return this.value / 10000000 +'千万';
                    }else if(this.value >= 1000000){
                        return this.value/1000000 +'百万';
                    }else if (this.value >= 10000){
                        return this.value/10000 +'万';
                    }else{
                       return this.value;
                    }
                }
                },
				plotLines: [{
					value: 0,
					width: 1,
					color: '#808080'
				}]
			},
			tooltip: {
				valueSuffix: '元'
			},
			legend: {
				enabled:false,
				layout: 'vertical',
				align: 'right',
				verticalAlign: 'middle',
				borderWidth: 0
			},
			exporting:{
                    enabled:false
                },
                credits: {
                    enabled: false
                },
			series: [{
				name: '投资金额',
				data: _data.totalInvests
			}]
		});
}
function refresh2(_data){
	//第2页面数据刷新
	// 饼图1
	$('#container2').highcharts({
		chart: {
			plotBackgroundColor: null,
			plotBorderWidth: null,
			plotShadow: false,
			width:charWidth
		},
		title: {
			text: '<div class="page-title">投资人/借款人分布</div>',
			useHTML:true,
			margin:50
		},
		tooltip: {
			pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b>'
		},		
		legend:{
			align:'left',
			verticalAlign:'top',
			x:0,
			y:40
		},
		plotOptions: {
			pie: {
				allowPointSelect: true,
                animation:true,
                cursor: 'pointer',
                dataLabels: {
					enabled: false,
                    format: '<b>{point.name}</b>: {point.percentage:.2f} %',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                },
                showInLegend: true
			}
		},
		exporting:{
                    enabled:false
                },
                credits: {
                    enabled: false
        },
		series: [{
			type: 'pie',
			name: '人数比例',
			data: [
				{name:'投资人',color:'#50b432',y:_data.totalInvestment},
				{name:'借款人',color:'#058dc7',y:_data.totalLoan}
			]
		}]
	});
}
function refresh3(_data){
	//第3页面数据刷新
	// 饼图2
	var temp = [];
	for(var i=0;i<_data.ageType.length;i++){
		temp[i] = {name:_data.ageType[i],y:_data.ageData[i]};
	}
	$('#container3').highcharts({
		chart: {
			plotBackgroundColor: null,
			plotBorderWidth: null,
			plotShadow: false,
			width:charWidth
		},
		title: {
			text: '<div class="page-title">平台用户年龄分布</div>',
			useHTML:true,
			margin:50
		},
		tooltip: {
			pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b>'
		},
		legend:{
			align:'left',
			verticalAlign:'top',
			x:0,
			y:40
		},
		plotOptions: {
			pie: {
				allowPointSelect: true,
                animation:true,
                cursor: 'pointer',
                dataLabels: {
					enabled: false,
                    format: '<b>{point.name}</b>: {point.percentage:.2f} %',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                },
                showInLegend: true
			}
		},
		exporting:{
                    enabled:false
                },
                credits: {
                    enabled: false
        },
		series: [{
			type: 'pie',
			name: '年龄比例',
			data: temp
		}]
	});
}
function refresh4(_data){
	//第4页面数据刷新
	// 饼图3
	var temp = [];
	for(var i=0;i<_data.timeLimtsType.length;i++){
		temp[i] = {name:_data.timeLimtsType[i],y:_data.timeLimtsData[i]};
	}
	$('#container4').highcharts({
		chart: {
			plotBackgroundColor: null,
			plotBorderWidth: null,
			plotShadow: false,
			width:charWidth
		},
		title: {
			text: '<div class="page-title">项目期限分布</div>',
			useHTML:true,
			margin:50
		},
		tooltip: {
			pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b>'
		},
		legend:{
			align:'left',
			verticalAlign:'top',
			x:0,
			y:40
		},
		plotOptions: {
			pie: {
				allowPointSelect: true,
                animation:true,
                cursor: 'pointer',
                dataLabels: {
					enabled: false,
                    format: '<b>{point.name}</b>: {point.percentage:.2f} %',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                },
                showInLegend: true
			}
		},
		exporting:{
                    enabled:false
                },
                credits: {
                    enabled: false
        },
		series: [{
			type: 'pie',
			name: '项目比例',
			data: temp
		}]
	});
}
function refresh5(_data){
	//第5页面数据刷新
	// 饼图4
	var temp = [];
	for(var i=0;i<_data.projectTypeType.length;i++){
		temp[i] = {name:_data.projectTypeType[i],y:_data.projectTypeData[i]};
	}
	$('#container5').highcharts({
		chart: {
			plotBackgroundColor: null,
			plotBorderWidth: null,
			plotShadow: false,
			width:charWidth
		},
		title: {
			text: '<div class="page-title">项目类型分布</div>',
			useHTML:true,
			margin:50
		},
		tooltip: {
			pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b>'
		},
		legend:{
			align:'left',
			verticalAlign:'top',
			x:0,
			y:40
		},
		plotOptions: {
			pie: {
				allowPointSelect: true,
                animation:true,
                cursor: 'pointer',
                dataLabels: {
					enabled: false,
                    format: '<b>{point.name}</b>: {point.percentage:.2f} %',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                },
                showInLegend: true
			}
		},
		exporting:{
                    enabled:false
                },
                credits: {
                    enabled: false
        },
		series: [{
			type: 'pie',
			name: '类型比例',
			data: temp
		}]
	});
}
function refresh6(data){
	//第6页面数据刷新
	$('#totalCompensatoryAmount').html(Number(data.totalCompensatoryAmount).toFixed(2)+'元');
	$('#maxUserLoanBalanceProportion').html(Number(data.maxUserLoanBalanceProportion).toFixed(2)+'%');
	$('#maxTenUsersLoanBalancePropertion').html(Number(data.maxTenUsersLoanBalancePropertion).toFixed(2)+'%');
	$('#loanOverdueBalanceAmount').html(Number(data.loanOverdueBalanceAmount).toFixed(2)+'元');
	$('#loanOverdueBalanceRate').html(Number(data.loanOverdueBalanceRate).toFixed(2)+'%');
	$('#loanBadDebtRate').html(Number(data.loanBadDebtRate).toFixed(2)+'%');
}
function clear(index){
	switch(index){
		case 0:
		  $('#container1').html('');
		  break;
		case 1:
		  //$('#container0').html('');
		  $('#container2').html('');
		  break;
		case 2:
		  $('#container1').html('');
		  $('#container3').html('');
		  break;
		case 3:
		  $('#container2').html('');
		  $('#container4').html('');
		  break;
		case 4:
		  $('#container3').html('');
		  $('#container5').html('');
		  break;
		case 5:
		  $('#container4').html('');
		  //$('#container6').html('');
		  break;
		case 6:
		  $('#container5').html('');
		  break;			
	}
}
var my_data = null;
/**初始化页面数据**/
function initData(data){
	var temp = Base64.decode(data);
	my_data =	eval("("+temp+")");
	refresh0(my_data);
	refresh1(my_data);
	refresh2(my_data);
	refresh3(my_data);
	refresh4(my_data);
	refresh5(my_data);
	refresh6(my_data);
}
$(function () {	
	//initData("eyJhZ2VUeXBlIjpbIjkw5ZCOIiwiODDlkI4iLCI3MOWQjiIsIjYw5ZCOIiwi5YW25LuWIl0sInByb2plY3RUeXBlRGF0YSI6WzQ1NjAwMCwxMzAwMCwwLDM2NDIzNl0sIm1heFRlblVzZXJzTG9hbkJhbGFuY2VQcm9wZXJ0aW9uIjoiODguNDEwMCIsInRvdGFsSW52ZXN0bWVudCI6MjMsImxvYW5PdmVyZHVlQmFsYW5jZUFtb3VudCI6Ijg2Ny40NCIsInRpbWVMaW10c0RhdGEiOlswLDIyNzgwMCwzOTQzNiwxMDAwMDAsMTIwMDAwLDMwMDAwXSwibG9hbk92ZXJkdWVCYWxhbmNlUmF0ZSI6IjAuMTcwMCIsIm1heFVzZXJMb2FuQmFsYW5jZVByb3BvcnRpb24iOiIyNS4yNzAwIiwidG90YWxSZWdpc3RlciI6MTAzLCJhZ2VEYXRhIjpbNSwxMCw2LDEyLDQyXSwicHJvamVjdFR5cGVUeXBlIjpbIuacuuaehOaLheS");
});