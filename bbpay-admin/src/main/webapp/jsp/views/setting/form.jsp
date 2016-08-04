<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i> 综合设置 <span class="divider"> <i class="icon-angle-right arrow-icon"></i>
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
<form class="form-horizontal" id="bizForm" method="post" action="/setting/">
	<input type="hidden"  name="id" value="${entity.id}" />
	<input type="hidden"  name="smsGetMobileEnabled" value="${entity.smsGetMobileEnabled}" />
	<input type="hidden"  name="smsGetMobileSendAddress" value="${entity.smsGetMobileSendAddress}" />
	<input type="hidden"  name="stepReportEnabled" value="${entity.stepReportEnabled}" />
	<div class="control-group">
		<label class="control-label" for="bizEnabled">全局计费开关</label>
		<div class="controls">
			<input type="checkbox"  id="bizEnabled" name="bizEnabled" value="1" ${entity.bizEnabled?"checked":""} /><span class="lbl"> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="blockReportEnabled">短信拦截报告开关</label>
		<div class="controls">
			<input type="checkbox"  id="blockReportEnabled" name="blockReportEnabled" value="1" ${entity.blockReportEnabled?"checked":""} /><span class="lbl"> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="showProcessDialog">计费进度条开关</label>
		<div class="controls">
			<input type="checkbox"  id="showProcessDialog" name="showProcessDialog" value="1" ${entity.showProcessDialog?"checked":""} /><span class="lbl"> </span>
		</div>
	</div>	
	<div class="control-group">
		<label class="control-label" for="blockExpireSeconds">短信拦截失效时间(秒)</label>
		<div class="controls">
			<input type="text" placeholder="短信拦截失效时间(秒)" id="blockExpireSeconds" name="blockExpireSeconds" value="${entity.blockExpireSeconds}" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="orderReportEnabled">计费报告开关</label>
		<div class="controls">
			<input type="checkbox"  id="orderReportEnabled" name="orderReportEnabled" value="1" ${entity.orderReportEnabled?"checked":""} /><span class="lbl"> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="orderTimeoutSeconds">计费超时时间(秒)</label>
		<div class="controls">
			<input type="text" placeholder="计费超时时间(秒)" id="orderTimeoutSeconds" name="orderTimeoutSeconds" value="${entity.orderTimeoutSeconds}" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="bizHost">主机地址</label>
		<div class="controls">
			<input type="text" placeholder="主机地址" id="bizHost" name="bizHost" value="${entity.bizHost}" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="clientPayDailyLimit">单机日限(分)</label>
		<div class="controls">
			<input type="text" placeholder="单机日限(分)" id="clientPayDailyLimit" name="clientPayDailyLimit" value="${entity.clientPayDailyLimit}" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="clientPayInterval">单机计费间隔(秒)</label>
		<div class="controls">
			<input type="text" placeholder="单机计费间隔(秒)" id="clientPayInterval" name="clientPayInterval" value="${entity.clientPayInterval}" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="commonBlockPorts">默认拦截端口号</label>
		<div class="controls">
			<input type="text" placeholder="默认拦截端口号" id="commonBlockPorts" name="commonBlockPorts" value="${entity.commonBlockPorts}" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="priceApproxDegree">价格匹配模糊度(%)</label>
		<div class="controls">
			<input type="text" placeholder="价格匹配模糊度" id="priceApproxDegree" name="priceApproxDegree" value="${entity.priceApproxDegree}" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="orderBizCount">代码组合条数</label>
		<div class="controls">
			<input type="text" placeholder="代码组合条数" id="orderBizCount" name="orderBizCount" value="${entity.orderBizCount}" />
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
		window.location = "/setting/"
	}
	var submitForm = function(btn) {
		btn.form.submit();
	}
</script>