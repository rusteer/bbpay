<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i><a href="/cp/${cp.id}">${cp.name}</a> <span class="divider"> <i class="icon-angle-right arrow-icon"></i>
		</span></li>
		<li><a href="/app/">游戏列表</a> <span class="divider"> <i class="icon-angle-right arrow-icon"></i></span></li>
		<li>游戏编辑页面 </li>
	</ul>
</div>
<c:if test="${param.saveSuccess==true}">
	<div class="alert alert-block alert-success">
		<button type="button" class="close" data-dismiss="alert">
			<i class="icon-remove"></i>
		</button>
		<i class="icon-ok green"></i> 保存成功!
	</div>
</c:if>
<c:if test="${saveSuccess==false}">
	<div class="alert alert-block alert-error">
		<button type="button" class="close" data-dismiss="alert">
			<i class="icon-remove"></i>
		</button>
		<i class="icon-remove"></i> 保存失败! ${errorMessage}
	</div>
</c:if>
<hr/>
<form class="form-horizontal" id="bizForm" method="post" action="/app/">
	<input type="hidden" name="cpId" value="${cp.id}"/>
	<div class="control-group">
		<label class="control-label" for="cpId">CP</label>
		<div class="controls">
			<input type="text" id="cpId" value="${cp.name}" readonly />
		</div>
	</div>	
	<div class="control-group">
		<label class="control-label" for="groupId">分组</label>
		<div class="controls">
			<select id="groupId" name="groupId">
					<option value="0"></option>
					<c:forEach var="group" items="${groupList}">
						 <option value="${group.id}" ${entity.groupId==group.id?"selected":""}>${group.name}</option>
					</c:forEach>
			</select>
		</div>
	</div>		
	<div class="control-group">
		<label class="control-label" for="id">ID</label>
		<div class="controls">
			<input type="text" placeholder="ID" id="id" name="id" value="${entity.id}" readonly />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="name">名称</label>
		<div class="controls">
			<input type="text" placeholder="名称" id="name" name="name" value="${entity.name}" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="description">简介</label>
		<div class="controls">
			<textarea id="description" name="description">${entity.description}</textarea>
		</div>
	</div>	
	<div class="form-actions">
		<button class="btn btn-info" type="button" onclick="submitForm(this)">
			<i class="icon-ok bigger-110"></i> 保存
		</button>
		&nbsp; &nbsp; &nbsp;
		<button class="btn" type="button" onclick="goBack()">
			<i class="icon-backward bigger-110"></i> 返回
		</button>
	</div>
</form>
<script type="text/javascript">
	var goBack = function() {
		window.history.back();
	}
	var submitForm = function(btn) {
		btn.form.submit();
	}
</script>