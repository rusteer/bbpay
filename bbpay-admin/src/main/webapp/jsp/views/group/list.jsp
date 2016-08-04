<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i> 分组管理  <span class="divider"> <i class="icon-angle-right arrow-icon"></i> </span></li>
		<li><a href="/${cmpName}/">分组列表</a> <span class="divider"> <i class="icon-angle-right arrow-icon"></i> </span></li>
	</ul>
</div>
<div class="page-content">
	<div class="row-fluid">
		<div class="span12">
			<div class="table-toolbar">
			   <div class="btn-group">
			       <a href="/${cmpName}/0" class="btn btn-small btn-success">新增分组</a>
			   </div>
			</div> 
			<div class="hr"></div>	 		
			<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-condensed table-hover" id="example2" >
			    <thead>
			        <tr>
			            <th>ID</th>
			            <th>名称</th>
			            <th>创建时间</th>
			        </tr>
			    </thead>
			    <tbody>
			    <c:forEach var="entity" items="${list}">
			    	 <tr class="odd gradeX">
			    	 	<td><a href="/${cmpName}/${entity.id}">${entity.id}</a></td>
			    	 	<td>${entity.name}</td>
			    	 	<td>${entity.createTime}</td>
			    	 </tr>
			    </c:forEach>
			    </tbody>
			</table>
		</div>
	</div>
</div>

	