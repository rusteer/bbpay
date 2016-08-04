<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i><a href="/cp/${cp.id}">${cp游.name}</a> <span class="divider"> <i class="icon-angle-right arrow-icon"></i>
		</span></li>
		<li>游戏列表 </li>
	</ul>
</div>
<div class="page-content">
	<div class="row-fluid">
		<div class="span12">
			<div class="table-toolbar">
				<form class="form-inline">
					<a href="/app/${cp.id}/0" class="btn btn-small btn-success">新增游戏</a>
					<!--  -->
					<select class="input-large" id="cp">
						<option value="">选择CP</option>
						<c:forEach var="element" items="${cpList}">
							<option value="${element.id}" ${cp.id==element.id?"selected":""}>${element.name}</option>
						</c:forEach>
					</select>
				</form>
			</div>
			<div class="hr"></div>
			<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-condensed table-hover" id="example2">
				<thead>
					<tr>
						<th>ID</th>
						<th>游戏名称</th>
						<th>分组</th>
						<th>游戏简介</th>
						<th>创建时间</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="entity" items="${list}">
						<tr class="odd gradeX">
							<td><a href="/app/${entity.cpId}/${entity.id}">${entity.id}</a></td>
							<td>${entity.name}</td>
							<td>
								<c:forEach var="group" items="${groupList}">
										<c:if test="${entity.groupId==group.id}">
						    	 			${group.name}
						    	 		</c:if>
								</c:forEach>
							</td>
							<td>${entity.description}</td>
							<td>${entity.createTime}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
<script>
	var refreshList = function() {
		var cpId = $("#cp").val();
		window.location.href = "/app/" + cpId;
	}
	$(function() {
		$("#cp").change(function() {
			refreshList();
		});
	});
</script>