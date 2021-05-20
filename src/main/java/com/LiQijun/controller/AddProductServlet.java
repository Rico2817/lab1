package com.LiQijun.controller;

import com.LiQijun.dao.ProductDao;
import com.LiQijun.model.Category;
import com.LiQijun.model.Product;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "AddProductServlet", value = "/admin/addProduct")
@MultipartConfig(maxFileSize = 16177215)
public class AddProductServlet extends HttpServlet {
    Connection con=null;
    @Override
    public void init() throws ServletException {
        con=(Connection) getServletContext().getAttribute("con");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Category> categoryList=Category.findAllCategory(con);
            request.setAttribute("categoryList",categoryList);
            String path="../WEB-INF/views/admin/addProduct.jsp";
            request.getRequestDispatcher(path).forward(request,response);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get all parameters
        String productName = request.getParameter("productName");
        double price= request.getParameter("price")!=null?Double.parseDouble(request.getParameter("price")):0.0;
        int categoryId=request.getParameter("categoryId")!=null?Integer.parseInt(request.getParameter("categoryId")):8;
        String productDescription = request.getParameter("productDescription");
        //picture
        InputStream inputStream=null;
        Part filePart=request.getPart("picture");
        if(filePart!=null){
            //prints out some information for debugging;
            System.out.println("File name:"+filePart.getName()+"Size:"+filePart.getSize()+"File Type:"+filePart.getContentType());
            inputStream=filePart.getInputStream();
        }
        //support@pubgsupport.zendesk.com
        //support@pubgsupport.zendesk.com
        //set into model
        Product product=new Product();
        product.setProductName(productName);
        product.setPrice(price);
        product.setProductDescription(productDescription);
        product.setCategoryId(categoryId);
        product.setPicture(inputStream);

        //call same in DAO
        ProductDao productDao=new ProductDao();
        try {
            int n=productDao.save(product,con);
            if(n>0)
                response.sendRedirect("productList");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //forward

    }
}
