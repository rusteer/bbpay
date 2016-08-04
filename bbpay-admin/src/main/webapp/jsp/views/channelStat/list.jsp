<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i>数据同步 <span class="divider"> <i class="icon-angle-right arrow-icon"></i> </span></li>
		<li><a href="/${cmpName}/">游戏同步</a> <span class="divider"> <i class="icon-angle-right arrow-icon"></i> </span></li>
	</ul>
</div>
<div class="page-content">
	<div class="row-fluid">
		<div class="span12">
			<div class="table-toolbar">
				<div class="btn-group">
					<input type="text" class="input-small" id="statDate" value="${statDate}" placeholder="日期">
					<select class="input-large" id="appId">
						<option value="0">游戏</option>
						<c:forEach var="entity" items="${appList}">
							<option value="${entity.id}" ${entity.id==param.appId?"selected":"" }>${entity.name}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="hr"></div>
			<div class="table-toolbar">
				<a href="/${cmpName}/autoSet?statDate=${statDate}&appId=${param.appId}" class="btn btn-small btn-success">执行自动分配</a>
			</div>
			<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-condensed table-hover" id="example2" >
			    <thead>
			        <tr>
			        	<th>日期</th>
			            <th>游戏</th>
			            <th>渠道</th>
			            <th>游戏请求计费金额(元)</th>
			            <th>成功计费金额(元)</th>
			            <th>失败计费金额(元)</th>
			            <th>取消计费金额(元)</th>
			            <th>同步金额(元)</th>
			            <th>自动分配金额(元)</th>
			            <th></th>
			        </tr>
			    </thead>
			    <tbody>
				<c:set var="orderMoney" value="0"/>
				<c:set var="successMoney" value="0"/>
				<c:set var="failureMoney" value="0"/>
				<c:set var="cancelMoney" value="0"/>
				<c:set var="statementMoney1" value="0"/>
				<c:set var="statementMoney2" value="0"/>			    
			    <c:forEach var="entity" items="${list}">
			    	<c:set var="stat" value="${entity.stat}"/>
			    	<c:set var="app" value="${entity.app}"/>
					<c:set var="orderMoney" value="${orderMoney+stat.orderMoney}"/>
					<c:set var="successMoney" value="${successMoney+stat.successMoney}"/>
					<c:set var="failureMoney" value="${failureMoney+stat.failureMoney}"/>
					<c:set var="cancelMoney" value="${cancelMoney+stat.cancelMoney}"/>
					<c:set var="statementMoney1" value="${statementMoney1+stat.statementMoney}"/>
					<c:set var="statementMoney2" value="${statementMoney2+entity.statementMoney}"/>	    			    	
			    	 <tr class="odd gradeX">
			    	 	<td>${stat.statDate}</td>
			    	 	<td>${app.name}</td>
			    	 	<td>${stat.channelId}</td>
			    	 	<td>${stat.orderMoney/100}</td>
			    	 	<td>${stat.successMoney/100}</td>
			    	 	<td>${stat.failureMoney/100}</td>
			    	 	<td>${stat.cancelMoney/100}</td>
			    	 	<td><fmt:formatNumber value="${stat.statementMoney/100}" pattern="##" /></td>
			    	 	<td><fmt:formatNumber value="${entity.statementMoney/100}" pattern="##" /></td>
			    	 	<td><a href="/${cmpName}/${stat.statDate}/${stat.appId}/${stat.channelId}">更新</a></td>
			    	 </tr>
			    </c:forEach>
			    	 <tr class="odd gradeX">
			    	 	<td colspan="3">总计</td>
			    	 	<td>${orderMoney/100}</td>
			    	 	<td>${successMoney/100}</td>
			    	 	<td>${failureMoney/100}</td>
			    	 	<td>${cancelMoney/100}</td>
			    	 	<td><fmt:formatNumber value="${statementMoney1/100}" pattern="##" /></td>
			    	 	<td><fmt:formatNumber value="${statementMoney2/100}" pattern="##" /></td>
			    	 	<td></td>
			    	 </tr>			    
			    </tbody>
			</table>
		</div>
	</div>
</div>
<script>
$(function() {
	 var idList=["statDate","appId"];
	 var refreshList=function(){
		 var url="/${cmpName}/?";
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
	