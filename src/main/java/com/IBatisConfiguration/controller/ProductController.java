package com.IBatisConfiguration.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.IBatisConfiguration.Entity.Product;
import com.IBatisConfiguration.Entity.UserDetails;
import com.IBatisConfiguration.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public void addProduct(@RequestBody Product product) {
        productService.addProduct(product);
    }

    @PutMapping("/{id}")
    public void updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        productService.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
    @GetMapping("/sp/{id}")
   public List<UserDetails> getAllProductsSP(@PathVariable Long id){
    	return productService.getUsersFromStoredProcedure(id);
    	
    }
    
    @GetMapping("/multipleResultSets/{id}")
    public List<UserDetails>  getMultipleResultSets(@PathVariable Map<String,Object> id) {
        return productService.getUsersAndRoles(id);
    }
    
    
    @GetMapping("/excel")
    public List<Map<String, Object>> excelToJson() {
        List<Map<String, Object>> jsonData = new ArrayList<>();

        try (FileInputStream excelFile = new FileInputStream(new File("C:\\Users\\Eluri Dasaradhi\\Desktop\\StaticProductDetails1.xlsx"));
             Workbook workbook = new XSSFWorkbook(excelFile)) {

            for (Sheet sheet : workbook) {
                Iterator<Row> rowIterator = sheet.iterator();

                // Read headers from the first row
                List<String> headers = new ArrayList<>();
                if (rowIterator.hasNext()) {
                    Row headerRow = rowIterator.next();
                    Iterator<Cell> cellIterator = headerRow.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        headers.add(cell.toString());
                    }
                }

                // Read data rows and create JSON objects
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    Map<String, Object> rowData = new LinkedHashMap<>();
                    int headerIndex = 0;

                    // Read data and create JSON object dynamically based on headers
                    while (cellIterator.hasNext() && headerIndex < headers.size()) {
                        Cell cell = cellIterator.next();
                        String header = headers.get(headerIndex);
                        rowData.put(header, cell.toString());
                        headerIndex++;
                    }

                    jsonData.add(rowData);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions and provide appropriate error messages
        }

        return jsonData;
    }
    
    @GetMapping("/excel2")
    public List<Map<String, Object>> excelToJson1() {
    	   List<Map<String, Object>> jsonData = new ArrayList<>();

           try (FileInputStream excelFile = new FileInputStream(new File("C:\\Users\\Eluri Dasaradhi\\Desktop\\StaticProductDetails.xlsx"));
                Workbook workbook = new XSSFWorkbook(excelFile)) {

               for (Sheet sheet : workbook) {
                   Iterator<Row> rowIterator = sheet.iterator();

                   if (rowIterator.hasNext()) {
                       rowIterator.next();
                   }

                   // Read headers from the first row
                   List<String> headers = new ArrayList<>();
                   if (rowIterator.hasNext()) {
                       Row headerRow = rowIterator.next();
                       Iterator<Cell> cellIterator = headerRow.cellIterator();
                       while (cellIterator.hasNext()) {
                           Cell cell = cellIterator.next();
                           headers.add(cell.toString());
                       }
                   }

                   // Read data rows and create JSON objects
                   while (rowIterator.hasNext()) {
                       Row row = rowIterator.next();
                       Iterator<Cell> cellIterator = row.cellIterator();

                       Map<String, Object> rowData = new LinkedHashMap<>();
                       int headerIndex = 0;

                       // Read data and create JSON object dynamically based on headers
                       while (cellIterator.hasNext() && headerIndex < headers.size()) {
                           Cell cell = cellIterator.next();
                           String header = headers.get(headerIndex);
                           rowData.put(header, cell.toString());
                           headerIndex++;
                       }

                       jsonData.add(rowData);
                   }
               }

           } catch (Exception e) {
               e.printStackTrace();
               // Handle exceptions and provide appropriate error messages
           }

           return jsonData;
       }
    @GetMapping("/ExcelCus")
    
    public List<Map<String, Object>> excelToJsonCustomised(@RequestParam List<String> specifiedHeaders) throws IOException {
        List<Map<String, Object>> jsonData = new ArrayList<>();

        try (FileInputStream excelFile = new FileInputStream(new File("C:\\Users\\Eluri Dasaradhi\\Desktop\\StaticProductDetails1.xlsx"));
             Workbook workbook = new XSSFWorkbook(excelFile)) {

            for (Sheet sheet : workbook) {
                Iterator<Row> rowIterator = sheet.iterator();

                // Read headers from the first row
                List<String> headers = new ArrayList<>();
                if (rowIterator.hasNext()) {
                    Row headerRow = rowIterator.next();
                    Iterator<Cell> cellIterator = headerRow.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        headers.add(cell.toString());
                    }
                } else {
                    throw new IOException("Excel sheet is empty. No headers found.");
                }

                // Find indices of specified headers
                List<Integer> specifiedHeaderIndices = new ArrayList<>();
                for (String specifiedHeader : specifiedHeaders) {
                    int index = headers.indexOf(specifiedHeader);
                    if (index != -1) {
                        specifiedHeaderIndices.add(index);
                    } else {
                        throw new IOException("Header '" + specifiedHeader + "' not found in the Excel sheet.");
                    }
                }

                // Read data rows and create JSON objects for specified columns
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Map<String, Object> rowData = new LinkedHashMap<>();

                    for (int headerIndex : specifiedHeaderIndices) {
                        Cell cell = row.getCell(headerIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null) {
                            rowData.put(headers.get(headerIndex), cell.toString());
                        } else {
                            rowData.put(headers.get(headerIndex), null);
                        }
                    }

                    jsonData.add(rowData);
                }
            }
        }

        return jsonData;
    }
    @GetMapping("/excelNYP")
    public List<Map<String, Object>> excelToJson(@RequestParam(required = false) List<String> specifiedHeaders) throws IOException {
        List<Map<String, Object>> jsonData = new ArrayList<>();

        try (FileInputStream excelFile = new FileInputStream(new File("C:\\Users\\Eluri Dasaradhi\\Desktop\\StaticProductDetails1.xlsx"));
             Workbook workbook = new XSSFWorkbook(excelFile)) {

            for (Sheet sheet : workbook) {
                Iterator<Row> rowIterator = sheet.iterator();

                // Read headers from the first row
                List<String> headers = new ArrayList<>();
                if (rowIterator.hasNext()) {
                    Row headerRow = rowIterator.next();
                    Iterator<Cell> cellIterator = headerRow.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        headers.add(cell.toString());
                    }
                } else {
                    throw new IOException("Excel sheet is empty. No headers found.");
                }

                // If specifiedHeaders is not provided or is empty, include all headers
                if (specifiedHeaders == null || specifiedHeaders.isEmpty()) {
                    specifiedHeaders = headers;
                }

                // Find indices of specified headers
                List<Integer> specifiedHeaderIndices = new ArrayList<>();
                for (String specifiedHeader : specifiedHeaders) {
                    int index = headers.indexOf(specifiedHeader);
                    if (index != -1) {
                        specifiedHeaderIndices.add(index);
                    } else {
                        throw new IOException("Header '" + specifiedHeader + "' not found in the Excel sheet.");
                    }
                }

                // Read data rows and create JSON objects for specified columns
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Map<String, Object> rowData = new LinkedHashMap<>();

                    for (int headerIndex : specifiedHeaderIndices) {
                        Cell cell = row.getCell(headerIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        if (cell != null) {
                            rowData.put(headers.get(headerIndex), cell.toString());
                        } else {
                            rowData.put(headers.get(headerIndex), null);
                        }
                    }

                    jsonData.add(rowData);
                }
            }
        }

        return jsonData;
    }
}
