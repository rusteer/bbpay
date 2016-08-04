<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i> 数据统计 <span class="divider"> <i class="icon-angle-right arrow-icon"></i>
		</span></li>
		<li><a href="/report/app/${subCmpName}">游戏数据统计->${groupName}分组</a> <span class="divider"> <i class="icon-angle-right arrow-icon"></i>
		</span></li>
	</ul>
</div>
<div class="page-content">
	<div class="row-fluid">
		<div class="span12">
			<div class="table-toolbar">
				<div class="btn-group">
					<input type="text" class="input-small" id="fromDate" value="${fromDate}" placeholder="起始日期"> 
					<input type="text" class="input-small" id="toDate" value="${toDate}" placeholder="结束日期"> 
					<select class="input-large" id="cpId">
						<option value="0">CP</option>
						<c:forEach var="entity" items="${cpList}">
							<option value="${entity.id}" ${entity.id==param.cpId?"selected":"" }>${entity.name}</option>
						</c:forEach>
					</select>		
					<select class="input-large" id="appId">
						<option value="0">游戏</option>
						<c:forEach var="entity" items="${appList}">
							<option value="${entity.id}" ${entity.id==param.appId?"selected":"" }>${entity.name}</option>
						</c:forEach>
					</select>
					<select class="input-large" id="channelId">
						<option value="0">渠道</option>
						<c:forEach var="entity" items="${channelList}">
							<option value="${entity.channelId}" ${entity.channelId==param.channelId?"selected":"" }>${entity.channelId}</option>
						</c:forEach>
					</select>
					<select class="input-large" id="provinceId">
						<option value="0">省份</option>
						<c:forEach var="entity" items="${provinceList}">
							<option value="${entity.id}" ${entity.id==param.provinceId?"selected":"" }>${entity.name}</option>
						</c:forEach>
					</select>
					<select class="input-large" id="carrierOperator">
							<option value="0">运营商</option>
							<option value="1" ${1==param.carrierOperator?"selected":"" }>移动</option>
							<option value="2" ${2==param.carrierOperator?"selected":"" }>联通</option>
							<option value="3" ${3==param.carrierOperator?"selected":"" }>电信</option>
						</select>																																		
					</div>
			</div>
			<div class="hr"></div>
			<c:choose>
				<c:when test="${empty list}">
					 <div class='alert alert-error'>无数据</div>
				</c:when>
				<c:otherwise>
					<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-condensed table-hover" id="example2">
						<thead>
							<tr>
								<th>${groupName}</th>
								<th>付费次数</th>
								<th>付费金额(元)</th>
								<th>成功金额(元)</th>
							</tr>
						</thead>
						<tbody>
							<c:set var="payCount" value="0"/>
							<c:set var="payMoney" value="0"/>
							<c:set var="successMoney" value="0"/>
							<c:forEach var="entity" items="${list}">
								<c:set var="payCount" value="${payCount+entity.payCount}"/>
								<c:set var="payMoney" value="${payMoney+entity.payMoney}"/>
								<c:set var="successMoney" value="${successMoney+entity.successMoney}"/>
								<tr class="odd gradeX">
									<td>${entity.group}</td>
									<td>${entity.payCount}</td>
									<td>${entity.payMoney/100}</td>
									<td>${entity.successMoney/100}</td>
								</tr>
							</c:forEach>
								<tr class="odd gradeX">
									<td>总计</td>
									<td>${payCount}</td>
									<td>${payMoney/100}</td>
									<td>${successMoney/100}</td>
								</tr>							
						</tbody>
					</table>				
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>
<script>
$(function() {
	 var idList=["fromDate","toDate","provinceId", "channelId", "appId", "cpId","carrierOperator"];
	 var refreshList=function(){
		 var url="/report/app/${subCmpName}?";
        _(idList).each(function(id){
        	url+="&"+id+"="+$("#"+id).val();
        });
        window.location.href=url;
    };  
   _(idList).each(function(id){
	   if(id.indexOf("Date")>0){
		$("#"+id).datepicker({format:"yyyy-mm-dd"}).on('changeDate', function(ev){refreshList();});
	   }else{
	    $("#"+id).change(function(){ refreshList(); });
	   }
   }); 
});
</script>
