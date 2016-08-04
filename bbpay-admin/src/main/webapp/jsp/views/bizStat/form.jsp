<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i> 数据同步 <span class="divider"> <i class="icon-angle-right arrow-icon"></i>
		</span></li>
		<li><a href="/${cmpName}/">代码同步</a> <span class="divider"> <i class="icon-angle-right arrow-icon"></i>
		</span></li>
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
<form class="form-horizontal" id="bizForm" method="post" action="/${cmpName}/">
	<input type="hidden" name="bizId" value="${biz.id}"/>
	<div class="control-group">
		<label class="control-label" for="statDate">日期</label>
		<div class="controls">
			<input type="text" placeholder="日期" id="statDate" name="statDate" value="${entity.statDate}" readonly/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="bizName">代码</label>
		<div class="controls">
			<input type="text" placeholder="代码" id="bizName"  value="${biz.name}" readonly/>
		</div>
	</div>	
	<div class="control-group">
		<label class="control-label" for="statementMoney">同步金额(元)</label>
		<div class="controls">
			<input type="text" placeholder="同步金额(元)" id="statementMoney" name="statementMoney" value='<fmt:formatNumber value="${entity.statementMoney/100}" pattern="##" />'  />
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