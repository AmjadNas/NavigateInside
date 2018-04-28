package com.navigate.inside.servlets;


import com.navigate.inside.database.operations.ConnPool;
import com.navigate.inside.database.operations.NodeResProvider;
import com.navigate.inside.objects.Node;
import com.navigate.inside.utils.FilesUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class ProjectResourceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final int GET_ALL_NODES_JSON_REQ = 0;


    private static final String RESOURCE_FAIL_TAG = "{\"result_code\":0}";
    private static final String RESOURCE_SUCCESS_TAG = "{\"result_code\":1}";

    private static final String REQ = "req";

    public static final int DB_RETRY_TIMES = 5;


    public void init(ServletConfig config) throws ServletException {

        super.init();

        String tmp = config.getServletContext().getInitParameter("localAppDir");
        if (tmp != null) {
            FilesUtils.appDirName = config.getServletContext().getRealPath(tmp);
            System.out.println(FilesUtils.appDirName);

        }

    }


    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String respPage = null;
        String userReq = req.getParameter(REQ);
        Connection conn = null;
        int retry = DB_RETRY_TIMES;

        if (userReq != null) {
            int reqNo = Integer.valueOf(userReq);
            System.out.println("ProjectResourceServlet:: req code ==>" + reqNo);

            while (retry > 0) {

                switch (reqNo) {
                    case GET_ALL_NODES_JSON_REQ: {
                        conn = ConnPool.getInstance().getConnection();
                        respPage = RESOURCE_FAIL_TAG;
                        NodeResProvider NodeProvider = new NodeResProvider();

                        List<Node> getAllNodes = null;
                        try {
                            getAllNodes = NodeProvider.getAllNodes(conn);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        String resultJson = Node.toJson(getAllNodes);

                        resp.addHeader("Content-Type",
                                "application/json; charset=UTF-8");
                        if (resultJson != null && !resultJson.isEmpty()) {
                            respPage = resultJson;

                        } else {
                            resp.sendError(404);
                        }
                        PrintWriter pw = resp.getWriter();
                        pw.write(respPage);
                        retry = 0;
                        break;

                    }
                    default: {
                        retry = 0;
                        break;
                    }

                }

            }


        }

    }





    }
