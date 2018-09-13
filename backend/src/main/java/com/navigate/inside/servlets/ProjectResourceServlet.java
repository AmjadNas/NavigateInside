package com.navigate.inside.servlets;

import com.navigate.inside.database.operations.ConnPool;
import com.navigate.inside.database.operations.NodeResProvider;
import com.navigate.inside.database.operations.RoomResProvider;
import com.navigate.inside.objects.Node;
import com.navigate.inside.objects.Room;
import com.navigate.inside.utils.Constants;
import com.navigate.inside.utils.FilesUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class ProjectResourceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final int GET_ALL_NODES_JSON_REQ = 0;
    private static final int GET_NODE_IMAGE = 1;
    private static final int DELETE_NODE = 5;
    private static final int ADD_ROOM_TO_NODE = 3;
    private static final int PAIR_NODES = 4;

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
                try {
                    switch (reqNo) {
                        case GET_ALL_NODES_JSON_REQ: {
                            conn = ConnPool.getInstance().getConnection();
                            respPage = RESOURCE_FAIL_TAG;
                            NodeResProvider NodeProvider = new NodeResProvider();

                            List<Node> getAllNodes = NodeProvider.getAllNodes(conn);

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
                        case GET_NODE_IMAGE: {
                            String id = req.getParameter(Constants.ID);
                            respPage = RESOURCE_FAIL_TAG;

                            conn = ConnPool.getInstance().getConnection();
                            NodeResProvider itemsResProvider = new NodeResProvider();

                            byte[] imgBlob = itemsResProvider.getImage(id, conn);

                            if (imgBlob != null && imgBlob.length > 0) {
                                ServletOutputStream os = resp.getOutputStream();
                                os.write(imgBlob);
                            } else {
                                resp.sendError(404);
                            }

                            retry = 0;
                            break;
                        }case PAIR_NODES:{
                            String first = req.getParameter(Constants.FirstID);
                            String second = req.getParameter(Constants.SecondID);
                            int dir = Integer.parseInt(req.getParameter(Constants.Direction));
                            respPage = RESOURCE_FAIL_TAG;

                            conn = ConnPool.getInstance().getConnection();
                            NodeResProvider nodeResProvider = new NodeResProvider();

                            if (nodeResProvider.pairNodes(first, second, dir, conn)){
                                respPage = RESOURCE_SUCCESS_TAG;
                            }

                            PrintWriter pw = resp.getWriter();
                            pw.write(respPage);

                            retry = 0;
                            break;
                        }case ADD_ROOM_TO_NODE:{
                            String nID = req.getParameter(Constants.BEACONID);
                            String number = req.getParameter(Constants.NUMBER);
                            String name = req.getParameter(Constants.NAME);

                            respPage = RESOURCE_FAIL_TAG;

                            conn = ConnPool.getInstance().getConnection();
                            RoomResProvider roomResProvider = new RoomResProvider();
                            Room room = new Room(number, name);

                            if (roomResProvider.insertRoom(nID, room, conn)) {
                                respPage = RESOURCE_SUCCESS_TAG;
                            }

                            PrintWriter pw = resp.getWriter();
                            pw.write(respPage);

                            retry = 0;
                            break;
                        }case DELETE_NODE:{

                            String nID = req.getParameter(Constants.BEACONID);

                            respPage = RESOURCE_FAIL_TAG;

                            conn = ConnPool.getInstance().getConnection();
                            NodeResProvider nodeResProvider = new NodeResProvider();

                            if (nodeResProvider.deleteItem(nID, conn)) {
                                respPage = RESOURCE_SUCCESS_TAG;
                            }

                            PrintWriter pw = resp.getWriter();
                            pw.write(respPage);

                            retry = 0;
                            break;
                        }
                        default: {
                            retry = 0;

                        }

                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                    retry--;
                } catch (Throwable t) {
                    t.printStackTrace();
                    retry = 0;
                } finally {
                    if (conn != null) {
                        ConnPool.getInstance().returnConnection(conn);
                    }
                }

            }
        }
    }

}
