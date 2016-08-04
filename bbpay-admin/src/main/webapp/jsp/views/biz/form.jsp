<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i> 代码管理 <span class="divider"> <i class="icon-angle-right arrow-icon"></i> </span></li>
		<li><a href="/biz/">代码列表</a> <span class="divider"> <i class="icon-angle-right arrow-icon"></i> </span></li>
		<li>${biz.id}.${biz.name}</li>
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
<form id="bizForm" method="post" action="/biz/">
	<div class="page-content">
		<ul class="nav nav-tabs" id="bizFormTab">
			<li><a href="#tab1" data-toggle="tab">基本属性</a></li>
			<li><a href="#tab2" data-toggle="tab">计费限制</a></li>
			<li><a href="#tab3" data-toggle="tab">开通省份</a></li>
			<li><a href="#tab4" data-toggle="tab">屏蔽地区</a></li>
			<li><a href="#tab5" data-toggle="tab">拦截与回复</a></li>
			<li><a href="#tab6" data-toggle="tab">客户端指令</a></li>
			<li><a href="#tab7" data-toggle="tab">服务端脚本</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
			<div class="tab-pane span10" id="tab1">
				<div class="row-fluid">
					<ul class="item-list">
						<li class="item-orange clearfix">
							<ul class="unstyled spaced2">
								<c:set var="bizId" value="${biz.id}"/>
								<c:if test="${param.copy=='1'}">
									<c:set var="bizId" value=""/>
								</c:if>
								<li><span class="span3">ID ${param.copy}</span><input class="span9" type="text" id="id" name="id" value='${bizId}' readonly="readonly"></li>
								<li><span class="span3">名称</span><input class="span9" type="text" id="name" name="name" value="${biz.name }"></li>
								<li><span class="span3">SP</span><select class="span9" id="spId" name="spId">
										<c:forEach var="sp" items="${spList}">
											 <option value="${sp.id}" ${biz.spId==sp.id?"selected":""}>${sp.name}</option>
										</c:forEach>
								</select></li>
								<li><span class="span3">分组</span><select class="span9" id="groupId" name="groupId">
										<option value="0"></option>
										<c:forEach var="group" items="${groupList}">
											 <option value="${group.id}" ${biz.groupId==group.id?"selected":""}>${group.name}</option>
										</c:forEach>
								</select></li>								
								<li><span class="span3">运营商</span> <select class="span9" id="carrierOperator" name="carrierOperator">
										<option value="1" ${biz.carrierOperator==1?"selected":""}>移动</option>
										<option value="2" ${biz.carrierOperator==2?"selected":""}>联通</option>
										<option value="3" ${biz.carrierOperator==3?"selected":""}>电信</option>
								</select></li>
								<li><span class="span3">端口号</span><input class="span9" type="text" id="port" name="port" value="${biz.port }"></li>
								<li><span class="span3">指令</span><input class="span9" type="text" id="command" name="command" value="${biz.command }"></li>
								<li><span class="span3">价格</span><input class="span9" type="text" id="price" name="price" value="${biz.price }"></li>
								<li><span class="span3">分成</span><input class="span9" type="text" id="sharing" name="sharing" value="${biz.sharing }"></li>
								<li><span class="span3">热度</span><input class="span9" type="text" id="hotLevel" name="hotLevel" value="${biz.hotLevel }"></li>
								<li><span class="span3">结算周期</span><input class="span9" type="text" id="paymentCycle" name="paymentCycle" value="${biz.paymentCycle }"></li>
								<li><span class="span3">状态</span><label class="span9"><input type="checkbox" name="enabled" ${biz.enabled?"checked":""} value="1"/><span class="lbl"></span></label></li>
							</ul>
						</li>
					</ul>
				</div>
			</div>
			<div class="tab-pane span10" id="tab2">
				<div class="row-fluid">
					<ul class="item-list">
						<li class="item-orange clearfix">
							<ul class="unstyled spaced2">
								<li><span class="span3 "><input class="span4 ace-switch" type="checkbox" />开始时间</span><input class="span9" type="text" id="startHour" name="startHour" value="${biz.startHour }"></li>
								<li><span class="span3">结束时间</span><input class="span9" type="text" id="endHour" name="endHour" value="${biz.endHour }"></li>
								<li><span class="span3">开始日期</span><input class="span9" type="text" id="startDate" name="startDate" value="${biz.startDate }"></li>
								<li><span class="span3">结束日期</span><input class="span9" type="text" id="endDate" name="endDate" value="${biz.endDate }"></li>
								<li><span class="span3">用户日限</span><input class="span9" type="text" id="deviceDailyMoney" name="deviceDailyMoney" value="${biz.deviceDailyMoney }"></li>
								<li><span class="span3">用户计费间隔</span><input class="span9" type="text" id="deviceInterval" name="deviceInterval" value="${biz.deviceInterval }"></li>
								<li><span class="span3">省份日限</span><input class="span9" type="text" id="provinceDailyMoney" name="provinceDailyMoney" value="${biz.provinceDailyMoney }"></li>
								<li><span class="span3">省份计费间隔</span><input class="span9" type="text" id="provinceInterval" name="provinceInterval" value="${biz.provinceInterval }"></li>
								<li><span class="span3">全国日限</span><input class="span9" type="text" id="globalDailyMoney" name="globalDailyMoney" value="${biz.globalDailyMoney }"></li>
								<li><span class="span3">全国计费间隔</span><input class="span9" type="text" id="globalInterval" name="globalInterval" value="${biz.globalInterval }"></li>
							</ul>
						</li>
					</ul>
				</div>
			</div>
			<div class="tab-pane span10" id="tab3">
				<div class="row-fluid">
					<c:forEach begin="0" end="3" var="i" >
						<div class="span3">
							<ul class="span12 unstyled spaced2">
									<c:forEach var="province" items="${provinceList}" begin="${i*8}" end="${(i+1)*8-1}">
										<c:set var="checked" value="" />
										<c:forEach var="element" items="${provinceLimits}">
											<c:if test="${element.provinceId==province.id}">
												<c:set var="checked"  value="checked"/>
												<c:set var="limit" value="${element}"/>
											</c:if>
										</c:forEach>
									<li class="item-orange clearfix">
										<label class="span4 inline"> <input type="checkbox" name="biz-allowProvince-id-${province.id}" ${checked} /> <span class="lbl"> ${province.name}</span></label> 
										<input class="span4" type="text" placeholder="日限" name="biz-allowProvince-provinceDailyMoney-${province.id}" value='${limit.dailyMoney>0?limit.dailyMoney:""}'> 
									</li>
									<c:remove var="limit"/>
								</c:forEach>
							</ul>
						</div>						 
					</c:forEach>
				</div>
			</div>
			<div class="tab-pane active span11" id="tab4">
				<div class="row-fluid">
					<c:forEach begin="0" end="3" var="i" >
						<div class="span3">
							<ul class="span12 unstyled spaced2">
									<c:forEach var="province" items="${provinceList}" begin="${i*8}" end="${(i+1)*8-1}">
										<c:set var="checked" value="" />
										<c:forEach var="element" items="${provinceLimits}">
											<c:if test="${element.provinceId==province.id}">
												<c:set var="checked"  value="checked"/>
												<c:set var="limit" value="${element}"/>
											</c:if>
										</c:forEach>
									<li class="item-orange clearfix">
										<!-- 
										<label class="span2 inline">${province.name}</label>
										 --> 
										<select multiple class="span2 chzn-select" data-placeholder="${province.name}" name="biz-allowProvince-disabledCity-${province.id}">
												<c:forEach var="city" items="${cityList}">
													<c:if test="${city.province.id==province.id }">
														<c:set var="selected" value="" />
														<c:forEach var="id" items="${limit.disabledCitySet}">
															<c:if test="${id==city.id}">
																<c:set value="selected" var="selected" />
															</c:if>
														</c:forEach>
														<option value="${city.id}" ${selected}>${city.name }</option>
													</c:if>
												</c:forEach>
										</select>
									</li>
									<c:remove var="limit"/>
								</c:forEach>
							</ul>
						</div>						 
					</c:forEach>
				</div>
				<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
			</div>
			<div class="tab-pane span10" id="tab5">
				<div class="row-fluid">
					<div class="span12">
							<ul class="span12 unstyled spaced2">
									<li class="item-orange clearfix">
										<label class="span2">拦截端口</label>
										<label class="span3">拦截关键字</label>
										<label class="span2">回复端口</label>
										<label class="span3">回复内容</label>
										<label class="span2">回复类型</label>
									</li>
									<c:forEach var="block" items="${blockList}" varStatus="status"> 
										<li class="item-orange clearfix">
											<input class="span2" type="text" name="blockPort-${status.count-1}" value="${block.blockPort}" />
											<input class="span3" type="text" name="blockContent-${status.count-1}" value="${block.blockContent}" />
											<input class="span2" type="text" name="replyPort-${status.count-1}" value="${block.replyPort}" />
											<input class="span3" type="text" name="replyContent-${status.count-1}" value="${block.replyContent}" />
											<select class="span2" name="replyType-${status.count-1}">
												<option/>
												<option value="1" ${block.replyType==1?"selected":""}>客户端</option>
												<option value="2" ${block.replyType==2?"selected":""}>服务端</option>
											</select> 
										</li>									
									</c:forEach>
							</ul>
					</div>
					 
				</div>
			</div>			
			<div class="tab-pane span10" id="tab6">
				<div class="row-fluid">
					<textarea class="span12" rows="25" name="clientOrder"><c:out value="${biz.clientOrder}"/></textarea>
				</div>
			</div>
			<div class="tab-pane span10" id="tab7">
				<div class="row-fluid">
					<textarea class="span12" rows="25" name="serviceScript"><c:out value="${biz.serviceScript}"/></textarea>
				</div>
			</div>
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
	$(function() {
		$(".chzn-select").chosen();
		$('#bizFormTab a:first').tab('show')
	});
	var goBack=function(){
		window.history.back();
	}
	var submitForm=function(btn){
		btn.form.submit();
	}
</script>