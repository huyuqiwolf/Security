package com.ken.study.security.file.servlet;

import com.ken.study.security.file.servlet.req.BaseRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件操作的servlet，使用原生的servlet演示部分案例
 */
public class FileServlet extends HttpServlet {

    private List<String> urlMatchers;
    private String rootPath = null;
    private String saveFilePath;

    @Override
    public void init() throws ServletException {
        //获取servlet处理的请求的uri的路径名称
        this.rootPath = getServletContext().getContextPath() + "/servlet/";
        saveFilePath  = getServletContext().getRealPath("/");
        initUrlMatchers();
    }

    /**
     * 初始化url的映射关系，相当于一个url的白名单
     */
    private void initUrlMatchers() {
        this.urlMatchers = new ArrayList<>();
        urlMatchers.add("uploadCheckSize");
        urlMatchers.add("uploadCheckType");
        urlMatchers.add("uploadCrossDir");
        urlMatchers.add("uploadUnzip");

    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals("POST")) {
            String msg = "仅支持POST请求";
            writeResponseToPage(resp, msg);
            return;
        }

        String requestUri = getRequestUrl(req.getRequestURI());

        if (!urlMatchers.contains(requestUri)) {
            String msg = "未知的请求";
            writeResponseToPage(resp, msg);
            return;
        }

        switch (requestUri) {
            case "uploadCheckSize":
                processCheckSize(req, resp);
                break;
            case "uploadCheckType":
                processCheckType(req, resp);
                break;
            case "uploadCrossDir":
                processCrossDir(req, resp);
                break;
            case "uploadUnzip":
                processUnzip(req, resp);
                break;
        }


//        writeResponseToPage(resp,requestUri);


    }

    private void processCheckType(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        writeResponseToPage(resp, getRequestUrl(req.getRequestURI()));
    }

    private void processCrossDir(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        writeResponseToPage(resp, getRequestUrl(req.getRequestURI()));
    }

    private void processUnzip(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        writeResponseToPage(resp, getRequestUrl(req.getRequestURI()));
    }

    private void processCheckSize(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        //1、创建一个DiskFileItemFactory工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //2、创建一个文件上传解析器
        ServletFileUpload upload = new ServletFileUpload(factory);
        //解决上传文件名的中文乱码
        upload.setHeaderEncoding("UTF-8");
        factory.setSizeThreshold(1024 * 500);//设置内存的临界值为500K
        File tempDir = new File("E:\\temp");//当超过500K的时候，存到一个临时文件夹中
        factory.setRepository(tempDir);
        upload.setSizeMax(1024 * 1024 * 2);//设置上传的文件总的大小不能超过2M

        try {
            // 1. 得到 FileItem 的集合 items
            List<FileItem> /* FileItem */items = upload.parseRequest(req);

            // 2. 遍历 items:
            for (FileItem item : items) {
                // 若是一个一般的表单域, 打印信息
                if (item.isFormField()) {
                    String name = item.getFieldName();
                    String value = item.getString("utf-8");
                }
                // 若是文件域则把文件保存到 e:\\files 目录下.
                else {
                    String fileName = item.getName();
                    long sizeInBytes = item.getSize();
                    System.out.println(fileName);
                    System.out.println(sizeInBytes);

                    InputStream in = item.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;

                    fileName = saveFilePath + fileName;//文件最终上传的位置
                    System.out.println(fileName);
                    OutputStream out = new FileOutputStream(fileName);

                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }

                    out.close();
                    in.close();
                }
            }
            writeResponseToPage(resp, "上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            writeResponseToPage(resp, "上传失败");
        }

    }

    /**
     * 获取请求的uri的最后一部分，判断是哪个form表单提交的请求
     *
     * @param requestURI
     * @return
     */
    private String getRequestUrl(String requestURI) {
        return requestURI.substring(rootPath.length());
    }

    private void writeResponseToPage(HttpServletResponse resp, String msg) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().write(msg);
    }

}
