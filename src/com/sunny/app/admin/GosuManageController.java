package com.sunny.app.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sunny.app.Execute;
import com.sunny.app.admin.dao.AdminDAO;
import com.sunny.app.admin.dao.GosuManageDAO;
import com.sunny.app.admin.dto.AdminDTO;
import com.sunny.app.admin.vo.GosuManageVO;
import com.sunny.app.admin.vo.HeaderInfoVO;
import com.sunny.app.util.AdminUtils;

public class GosuManageController implements Execute {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 세션체크
		int adminNumber = 0;
		if (AdminUtils.sessionCheck(req) == 0) {
			resp.sendRedirect("/admin/login.ad?login=noInfo");
			return;
		} else {
			adminNumber = AdminUtils.sessionCheck(req);
		}

		System.out.println("adminNumber = " + adminNumber);
		AdminDAO adminDAO = new AdminDAO();
		AdminDTO adminDTO = adminDAO.adminSelect(adminNumber);

		String searchWord = req.getParameter("searchWord");

		GosuManageDAO gosuManageDAO = new GosuManageDAO();
		HeaderInfoVO headerinfoVO = adminDAO.headerInfo();

		int total = gosuManageDAO.getTotal();
		String temp = req.getParameter("page");
		int page = temp == null ? 1 : Integer.valueOf(temp);

//	    한 페이지에 몇 개의 게시물? 10개
		int rowCount = 20;
//	    페이지 버튼 세트는? 5개씩
		int pageCount = 5;
		int startRow = (page - 1) * rowCount;
		int endPage = (int) (Math.ceil(page / (double) pageCount) * pageCount);
		int startPage = endPage - (pageCount - 1);
		int realEndPage = (int) Math.ceil(total / (double) rowCount);
		endPage = endPage > realEndPage ? realEndPage : endPage;
		boolean prev = startPage > 1;
		boolean next = endPage != realEndPage;

		// 고수 회원 정보 불러오기
		Map<String, Object> pageMap = new HashMap<>();

		pageMap.put("searchWord", searchWord);
		//
		pageMap.put("startRow", startRow);
		pageMap.put("rowCount", rowCount);
		//
		List<GosuManageVO> gosu = gosuManageDAO.gosuInfo(pageMap);

		// 데이터 세팅
		req.setAttribute("adminInfo", adminDTO);
		req.setAttribute("headerInfo", headerinfoVO);
		req.setAttribute("gosuList", gosu);
		//
		req.setAttribute("page", page);
		req.setAttribute("startPage", startPage);
		req.setAttribute("endPage", endPage);
		req.setAttribute("prev", prev);
		req.setAttribute("next", next);

		// 페이지 이동
		req.getRequestDispatcher("/app/admin/gosuManage.jsp").forward(req, resp);

	}
}
