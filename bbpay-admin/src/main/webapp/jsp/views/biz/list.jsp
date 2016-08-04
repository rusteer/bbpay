<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i> 代码管理 <span
			class="divider"> <i class="icon-angle-right arrow-icon"></i>
		</span></li>
		<li><a href="/biz/">代码列表</a> <span class="divider"> <i
				class="icon-angle-right arrow-icon"></i>
		</span></li>
	</ul>
</div>
<div class="page-content">
	<div class="row-fluid">
		<div class="span12">
			<div class="table-toolbar">
				<form class="form-inline" autocomplete="off">
					<a href="/biz/0" class="btn btn-small btn-success">新增代码</a>
					<input type="text" class="input-small" id="name" value="${param.name}" placeholder="名称"> 
					<input type="text" class="input-small" id="priceFrom" value="${param.priceFrom}" placeholder="最低价格">
					<input type="text" class="input-small" id="priceTo" value="${param.priceTo}" placeholder="最高价格"> 
					<select class="input-large" id="spId">
						<option value="0">选择SP</option>
						<c:forEach var="entity" items="${spList}">
							<option value="${entity.id}" ${entity.id==param.spId?"selected":"" }>${entity.name}</option>
						</c:forEach>
					</select>
					<select class="input-large" id="groupId">
						<option value="0">选择分组</option>
						<c:forEach var="entity" items="${groupList}">
							<option value="${entity.id}" ${entity.id==param.groupId?"selected":"" }>${entity.name}</option>
						</c:forEach>
					</select>
					<select class="input-large" id="enabled">
						<option value="">选择状态</option>
						<option value="1" ${1==param.enabled?"selected":"" }>开通</option>
						<option value="0" ${0==param.enabled?"selected":"" }>关闭</option>
					</select>																
				</form>
			</div>
			<div class="hr"></div>
			<table cellpadding="0" cellspacing="0" border="0"
				class="table table-striped table-bordered table-condensed table-hover"
				id="example2">
				<thead>
					<tr>
						<th>ID</th>
						<th>名称</th>
						<th>SP</th>
						<th>分组</th>
						<th>价格</th>
						<th>分成</th>
						<th>运营商</th>
						<th>操作</th>
						<th>端口号</th>
						<th>热度</th>
						<th>用户日限</th>
						<th>省份日限</th>
						<th>全国日限</th>
						<th>开始日期</th>
						<th>结束日期</th>
						<th>开始时间</th>
						<th>结束时间</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="biz" items="${list}">
						<tr class='odd gradeX ${biz.enabled?"text-success":"text-error"}'>
							<td><a href="/biz/${biz.id}">${biz.id}</a></td>
							<td>${biz.name}</td>
							<td><c:forEach var="sp" items="${spList}"><c:if test="${biz.spId==sp.id}">${sp.name}</c:if></c:forEach></td>
							<td><c:forEach var="group" items="${groupList}"><c:if test="${biz.groupId==group.id}">${group.name}</c:if></c:forEach></td>							
							<td>${biz.price}</td>
							<td>${biz.sharing}</td>
							<td>${biz.carrierOperator==1?"移动":(biz.carrierOperator==2?"联通":"电信")}</td>
							<td><c:choose>
									<c:when test="${biz.enabled}">
										<a href="/biz/${biz.id}/close">关闭</a>
									</c:when>
									<c:otherwise>
										<a href="/biz/${biz.id}/open">开通</a>
									</c:otherwise>
								</c:choose></td>
							<td>${biz.port}</td>
							<td>${biz.hotLevel}</td>
							<td>${biz.deviceDailyMoney}</td>
							<td>${biz.provinceDailyMoney}</td>
							<td>${biz.globalDailyMoney}</td>
							<td>${biz.startDate}</td>
							<td>${biz.endDate}</td>
							<td>${biz.startHour}</td>
							<td>${biz.endHour}</td>
							<td><a href="/biz/${biz.id}?copy=1">复制</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
<script>
$(function() {
	 var idList=["name","priceFrom","priceTo", "spId", "groupId","enabled"];
	 var refreshList=function(){
        var url="?";
        _(idList).each(function(id){
        	url+="&"+id+"="+encodeURIComponent($("#"+id).val());
        });
        window.location.href=url;
    };  
   _(idList).each(function(id){
	    $("#"+id).change(function(){ refreshList(); });
   }); 
});
</script>