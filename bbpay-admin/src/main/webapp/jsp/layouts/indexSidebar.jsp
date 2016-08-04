<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="activate" value="class='active open'"/>
<div class="sidebar" id="sidebar">
	<ul class="nav nav-list">
		<li ${cmpName=="group"?activate:""}><a href="/group/"> <i class="icon-list-alt"></i> <span class="menu-text">分组管理</span> </a></li>
		<li ${cmpName=="sp"?activate:""}><a href="/sp/"> <i class="icon-list-alt"></i> <span class="menu-text">SP管理</span> </a></li>
		<li ${cmpName=="biz"?activate:""}><a href="/biz/"> <i class="icon-list-alt"></i> <span class="menu-text">代码管理</span> </a></li>
		<li ${cmpName=="cp"?activate:""}><a href="/cp/"> <i class="icon-list-alt"></i> <span class="menu-text">CP管理</span></a></li>
		<li ${cmpName=="app"?activate:""}><a href="/app/"> <i class="icon-list"></i> <span class="menu-text">游戏管理</span></a></li>
		<li ${cmpName=="setting"?activate:""}><a href="/setting/"> <i class="icon-list-alt"></i> <span class="menu-text">设置</span> </a></li>
		<li ${(fn:indexOf(cmpName,"Report")>0 ) ?activate:""}><a href="#" class="dropdown-toggle"> <i class="icon-list-alt"></i> <span class="menu-text"> 统计 </span> <b class="arrow icon-angle-down"></b> </a>
			<ul class="submenu">
				<li ${ activate }><a href="#" class="dropdown-toggle"> <i class="icon-list-alt"></i> <span class="menu-text">计费数据 </span> <b class="arrow icon-angle-down"></b> </a>
					<ul class="submenu">
						<li ${cmpName=="bizReport-price"?activate:""}><a href="/report/biz/price"> <i class="icon-list"></i> <span class="menu-text">价格分组</span></a></li>
						<li ${cmpName=="bizReport-date"?activate:""}><a href="/report/biz/date"> <i class="icon-list"></i> <span class="menu-text">日期分组</span></a></li>
						<li ${cmpName=="bizReport-biz"?activate:""}><a href="/report/biz/biz"> <i class="icon-list"></i> <span class="menu-text">代码分组</span></a></li>
						<li ${cmpName=="bizReport-province"?activate:""}><a href="/report/biz/province"> <i class="icon-list"></i> <span class="menu-text">省份分组</span></a></li>
					</ul>
				</li>				
				<li ${ activate }><a href="#" class="dropdown-toggle"> <i class="icon-list-alt"></i> <span class="menu-text"> 游戏数据 </span> <b class="arrow icon-angle-down"></b> </a>
					<ul class="submenu">
						<li ${cmpName=="appReport-price"?activate:""}><a href="/report/app/price"> <i class="icon-list-alt"></i> <span class="menu-text">价格分组</span></a></li>
						<li ${cmpName=="appReport-date"?activate:""}><a href="/report/app/date"> <i class="icon-list-alt"></i> <span class="menu-text">日期分组</span></a></li>
					</ul>
				</li>							
			</ul>
		</li>
		<li  ${(fn:indexOf(cmpName,"Stat")>0 ) ?activate:""}><a href="#" class="dropdown-toggle"> <i class="icon-list-alt"></i> <span class="menu-text"> 数据同步 </span> <b class="arrow icon-angle-down"></b> </a>
			<ul class="submenu">
				<li ${cmpName=="bizStat"?activate:""}><a href="/bizStat/"> <i class="icon-list-alt"></i> <span class="menu-text">代码同步</span></a></li>
				<li ${cmpName=="channelStat"?activate:""}><a href="/channelStat/"> <i class="icon-list-alt"></i> <span class="menu-text">游戏同步</span></a></li>
			</ul>
		</li>						
	</ul>
	<div class="sidebar-collapse" id="sidebar-collapse"> <i class="icon-double-angle-left"></i></div>
</div>

	