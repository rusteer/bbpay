<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i> 数据统计<span class="divider"> <i class="icon-angle-right arrow-icon"></i>
		</span></li>
		<li><a href="/report/biz/${subCmpName}">计费数据统计->${groupName}分组</a> <span class="divider"> <i class="icon-angle-right arrow-icon"></i>
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
					<select class="input-large" id="bizId">
						<option value="0">代码</option>
						<c:forEach var="entity" items="${bizList}">
							<option value="${entity.id}" ${entity.id==param.bizId?"selected":"" }>${entity.name}</option>
						</c:forEach>
					</select>
					<select class="input-large" id="spId">
						<option value="0">SP</option>
						<c:forEach var="entity" items="${spList}">
							<option value="${entity.id}" ${entity.id==param.spId?"selected":"" }>${entity.name}</option>
						</c:forEach>
					</select>	
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
						<th rowspan="2">${groupName}</th>
						<th colspan="3" style="text-align: center"><div class="text-success">成功统计</div></th>
						<th colspan="3" style="text-align: center"><div class="text-error">失败统计</div></th>
						<th colspan="3" style="text-align: center"><div class="text-warning">取消统计</div></th>
						<th colspan="3" style="text-align: center"><div class="text-error">异常统计</div></th>
					</tr>
					<tr>
						<th style="text-align: center"><div class="text-success">条数</div></th>
						<th style="text-align: center"><div class="text-success">金额</div></th>
						<th style="text-align: center"><div class="text-success">比例</div></th>
						<th style="text-align: center"><div class="text-error">条数</div></th>
						<th style="text-align: center"><div class="text-error">金额</div></th>
						<th style="text-align: center"><div class="text-error">比例</div></th>
						<th style="text-align: center"><div class="text-warning">条数</div></th>
						<th style="text-align: center"><div class="text-warning">金额</div></th>
						<th style="text-align: center"><div class="text-warning">比例</div></th>
						<th style="text-align: center"><div class="text-error">条数</div></th>
						<th style="text-align: center"><div class="text-error">金额</div></th>
						<th style="text-align: center"><div class="text-error">比例</div></th>
					</tr>
				</thead>
				<tbody>
				
					<c:set var="allAllCount" value="0"/>
					<c:set var="successCount" value="0"/>
					<c:set var="successSum" value="0"/>
					<c:set var="failureCount" value="0"/>
					<c:set var="failureSum" value="0"/>
					<c:set var="cancelCount" value="0"/>
					<c:set var="cancelSum" value="0"/>
					<c:set var="unknownCount" value="0"/>
					<c:set var="unknownSum" value="0"/>
					
				
					<c:forEach var="entity" items="${list}">
						<c:set var="allCount" value="${entity.successCount+entity.failureCount+entity.cancelCount+entity.unknownCount}" />
						
						<c:set var="allAllCount" value="${allAllCount+allCount}"/>
						<c:set var="successCount" value="${successCount+entity.successCount}"/>
						<c:set var="successSum" value="${successSum+entity.successSum}"/>
						<c:set var="failureCount" value="${failureCount+entity.failureCount}"/>
						<c:set var="failureSum" value="${failureSum+entity.failureSum}"/>
						<c:set var="cancelCount" value="${cancelCount+entity.cancelCount}"/>
						<c:set var="cancelSum" value="${cancelSum+entity.cancelSum}"/>
						<c:set var="unknownCount" value="${unknownCount+entity.unknownCount}"/>
						<c:set var="unknownSum" value="${unknownSum+entity.unknownSum}"/>
						
						
						<tr class="odd gradeX">
							<td>${entity.group}</td>
							<td style="text-align: center"><div class="text-success">${entity.successCount}</div></td>
							<td style="text-align: center"><div class="text-success">${entity.successSum/100}</div></td>
							<td style="text-align: center">
								<div class="text-success">
									<c:if test="${entity.successCount>0}">
										<fmt:formatNumber value="${entity.successCount/allCount}" type="percent" maxFractionDigits="2" minFractionDigits="2" />
									</c:if>
								</div>
							</td>
							<td style="text-align: center"><div class="text-error">${entity.failureCount}</div></td>
							<td style="text-align: center"><div class="text-error">${entity.failureSum/100}</div></td>
							<td style="text-align: center">
								<div class="text-error">
									<c:if test="${entity.failureCount>0}">
										<fmt:formatNumber value="${entity.failureCount/allCount}" type="percent" maxFractionDigits="2" minFractionDigits="2" />
									</c:if>
								</div>
							</td>
							<td style="text-align: center"><div class="text-warning">${entity.cancelCount}</div></td>
							<td style="text-align: center"><div class="text-warning">${entity.cancelSum/100}</div></td>
							<td style="text-align: center">
								<div class="text-warning">
									<c:if test="${entity.cancelCount>0}">
										<fmt:formatNumber value="${entity.cancelCount/allCount}" type="percent" maxFractionDigits="2" minFractionDigits="2" />
									</c:if>
								</div>
							</td>
							<td style="text-align: center"><div class="text-error">${entity.unknownCount}</div></td>
							<td style="text-align: center"><div class="text-error">${entity.unknownSum/100}</div></td>
							<td style="text-align: center">
								<div class="text-error">
									<c:if test="${entity.unknownCount>0}">
										<fmt:formatNumber value="${entity.unknownCount/allCount}" type="percent" maxFractionDigits="2" minFractionDigits="2" />
									</c:if>
								</div>
							</td>
						</tr>
					</c:forEach>
						<tr class="odd gradeX">
							<td>总计</td>
							<td style="text-align: center"><div class="text-success">${successCount}</div></td>
							<td style="text-align: center"><div class="text-success">${successSum/100}</div></td>
							<td style="text-align: center">
								<div class="text-success">
									<c:if test="${successCount>0}">
										<fmt:formatNumber value="${successCount/allAllCount}" type="percent" maxFractionDigits="2" minFractionDigits="2" />
									</c:if>
								</div>
							</td>
							<td style="text-align: center"><div class="text-error">${failureCount}</div></td>
							<td style="text-align: center"><div class="text-error">${failureSum/100}</div></td>
							<td style="text-align: center">
								<div class="text-error">
									<c:if test="${failureCount>0}">
										<fmt:formatNumber value="${failureCount/allAllCount}" type="percent" maxFractionDigits="2" minFractionDigits="2" />
									</c:if>
								</div>
							</td>
							<td style="text-align: center"><div class="text-warning">${cancelCount}</div></td>
							<td style="text-align: center"><div class="text-warning">${cancelSum/100}</div></td>
							<td style="text-align: center">
								<div class="text-warning">
									<c:if test="${cancelCount>0}">
										<fmt:formatNumber value="${cancelCount/allAllCount}" type="percent" maxFractionDigits="2" minFractionDigits="2" />
									</c:if>
								</div>
							</td>
							<td style="text-align: center"><div class="text-error">${unknownCount}</div></td>
							<td style="text-align: center"><div class="text-error">${unknownSum/100}</div></td>
							<td style="text-align: center">
								<div class="text-error">
									<c:if test="${unknownCount>0}">
										<fmt:formatNumber value="${unknownCount/allAllCount}" type="percent" maxFractionDigits="2" minFractionDigits="2" />
									</c:if>
								</div>
							</td>
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
	 var idList=["fromDate","toDate","bizId", "provinceId", "channelId", "appId", "cpId", "spId", "carrierOperator"];
	 var refreshList=function(){
        var url="/report/biz/${subCmpName}?";
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
