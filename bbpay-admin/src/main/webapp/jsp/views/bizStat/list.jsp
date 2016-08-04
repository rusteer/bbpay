<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i>数据同步 <span class="divider"> <i class="icon-angle-right arrow-icon"></i> </span></li>
		<li><a href="/${cmpName}/">代码同步</a> <span class="divider"> <i class="icon-angle-right arrow-icon"></i> </span></li>
	</ul>
</div>
<div class="page-content">
	<div class="row-fluid">
		<div class="span12">
			<div class="table-toolbar">
				<div class="btn-group">
					<input type="text" class="input-small" id="statDate" value="${statDate}" placeholder="起始日期">
				</div>
			</div>
			<div class="hr"></div>	 		
			<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-condensed table-hover" id="example2" >
			    <thead>
			        <tr>
			        	<th>日期</th>
			            <th>代码</th>
			            <th>游戏请求计费金额(元)</th>
			            <th>成功计费金额(元)</th>
			            <th>失败计费金额(元)</th>
			            <th>取消计费金额(元)</th>
			            <th>同步金额(元)</th>
			            <th></th>
			        </tr>
			    </thead>
			    <tbody>
			    <c:set var="orderMoney" value="0"/>
			    <c:set var="successMoney" value="0"/>
			    <c:set var="failureMoney" value="0"/>
			    <c:set var="cancelMoney" value="0"/>
			    <c:set var="statementMoney" value="0"/>
			    <c:forEach var="entity" items="${list}">
			    	<c:set var="stat" value="${entity.value.stat}"/>
			    	<c:set var="biz" value="${entity.value.biz}"/>
			    	 <c:set var="orderMoney" value="${orderMoney+stat.orderMoney}"/>
			    	 <c:set var="successMoney" value="${successMoney+stat.successMoney}"/>
			    	 <c:set var="failureMoney" value="${failureMoney+stat.failureMoney}"/>
			    	 <c:set var="cancelMoney" value="${cancelMoney+stat.cancelMoney}"/>
			    	 <c:set var="statementMoney" value="${statementMoney+stat.statementMoney}"/>
			    	
			    	 <tr class="odd gradeX">
			    	 	<td>${stat.statDate}</td>
			    	 	<td>${biz.name}</td>
			    	 	<td>${stat.orderMoney/100}</td>
			    	 	<td>${stat.successMoney/100}</td>
			    	 	<td>${stat.failureMoney/100}</td>
			    	 	<td>${stat.cancelMoney/100}</td>
			    	 	<td><fmt:formatNumber value="${stat.statementMoney/100}" pattern="##" /></td>
			    	 	<td><a href="/${cmpName}/${stat.statDate}/${biz.id}">更新</a></td>
			    	 </tr>
			    </c:forEach>
 					<tr class="odd gradeX">
			    	 	<td colspan="2">总计</td>
			    	 	<td>${orderMoney/100}</td>
			    	 	<td>${successMoney/100}</td>
			    	 	<td>${failureMoney/100}</td>
			    	 	<td>${cancelMoney/100}</td>
			    	 	<td><fmt:formatNumber value="${statementMoney/100}" pattern="##" /></td>
			    	 	<td></td>
			    	 </tr>			    
			    </tbody>
			</table>
		</div>
	</div>
</div>
<script>
$(function() {
	 var idList=["statDate"];
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
	