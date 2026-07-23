
package com.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ValueChangeToExcel {

    public static void main(String[] args) {
        List<AttrInfoVo> CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE = new ArrayList<>();
//
//        //子节点中从服务信息中增加的属性
        List<AttrInfoVo> CHN_SUB_SERVICE_PROPERTY_ATTR_CODE = new ArrayList<>();
//
//
        //bandwidth又新增了
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("gateway_bandwidth_411").attrName("Download Bandwidth").targetAttrCode("PM_DOWNLOAD_BANDWIDTH").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("gateway_bandwidth_411").attrName("Upload Bandwidth").targetAttrCode("PM_UPLOAD_BANDWIDTH").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("vdom_bandwidth_411").attrName("Download Bandwidth").targetAttrCode("PM_DOWNLOAD_BANDWIDTH").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("vdom_bandwidth_411").attrName("Upload Bandwidth").targetAttrCode("PM_UPLOAD_BANDWIDTH").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("bandwidth_411").attrName("Download Bandwidth").targetAttrCode("PM_DOWNLOAD_BANDWIDTH").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("bandwidth_411").attrName("Upload Bandwidth").targetAttrCode("PM_UPLOAD_BANDWIDTH").build());



        //bandwidth_burstable_411
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("bandwidth_burstable_411").attrName("Customer Download Bandwidth").targetAttrCode("PM_CUSTOMER_DOWNLOAD_BANDWIDTH").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("bandwidth_burstable_411").attrName("Customer Upload Bandwidth").targetAttrCode("PM_CUSTOMER_UPLOAD_BANDWIDTH").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("vpn_type_411").attrName("Product Type").targetAttrCode("PM_PRODUCT_TYPE").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("voice_gateway_411").attrName("Voice Gateway").targetAttrCode("PM_VOICE_GATEWAY").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("protocol_411").attrName("Protocol").targetAttrCode("PM_CHINA_L2VPN_PROTOCOL").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("service_element_type_411").attrName("Service Type").targetAttrCode("PM_SERVICE_TYPE").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("coverage").attrName("Coverage").targetAttrCode("PM_COVERAGE").build());

        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("cloud_provider").attrName("Cloud Provider").targetAttrCode("PM_CLOUD_PROVIDER").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("cloud_region").attrName("Cloud Region").targetAttrCode("PM_CLOUD_REGIOND").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("cloud_portal").attrName("Cloud Portal").targetAttrCode("PM_CLOUD_PORTAL").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("cloud_asn").attrName("Cloud Asn").targetAttrCode("PM_CLOUD_ASN").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("cloud_peering").attrName("Cloud Peering").targetAttrCode("PM_CLOUD_PEERING").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("cloudtocloud").attrName("Cloud To Cloud").targetAttrCode("PM_CLOUD_TO_CLOUD").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("bfd").attrName("BFD").targetAttrCode("PM_BFD").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("account_service_element_key").attrName("Cloud Account ID").targetAttrCode("PM_CLOUD_ACCOUNT_ID").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("route_summary").attrName("Route Summary").targetAttrCode("RM_ROUTE_SUMMARY").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("line_type").attrName("Line Type").targetAttrCode("PM_LINE_TYPE").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("connect_to_411").attrName("Network Type").targetAttrCode("PM_NETWORK_TYPE").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("Cloud_Port_Provider").attrName("Cloud Port Provider").targetAttrCode("PM_CLOUD_PORT_PROVIDER").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("region_411").attrName("Region").targetAttrCode("PM_CHINA_REGION").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("site").attrName("Location").targetAttrCode("PM_CHINA_LOCATION").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("user_name_411").attrName("User Name").targetAttrCode("PM_USER_NAME").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("pI_internet_option").attrName("Sub Product Type").targetAttrCode("PM_SUB_PRODUCT_TYPE").build());
        CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("ip_address_411").attrName("IP Address").targetAttrCode("PM_CHINA_PI_IP").build());


        //DDos
        CHN_SUB_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("protection_cap_gbps_411").attrName("Protection Capacity").targetAttrCode("PM_PROTECTION_CAPACITY").build());
        CHN_SUB_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("ddos_411").attrName("Drill Test").targetAttrCode("PM_DRILL_TEST").build());
        //CrossConnect
        CHN_SUB_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("carrier_customer_rack_411").attrName("Carrier Equipment to Customer Rack").targetAttrCode("PM_CROSS_CARRIER_CUST_RACK").build());
        CHN_SUB_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("customer_hkt_rack_to_rack_411").attrName("Customer (HKT) Rack to Rack").targetAttrCode("PM_CROSS_CUST_HKT_RACK2RACK").build());

//
//


        exportToExcel(CHN_MAIN_SERVICE_PROPERTY_ATTR_CODE, "ADD_CHN_Main_Service_Property_Attr.xlsx");
        exportToExcel(CHN_SUB_SERVICE_PROPERTY_ATTR_CODE, "ADD_CHN_SUB_Service_Property_Attr.xlsx");
    }

    private static void exportToExcel(List<AttrInfoVo> dataList, String fileName) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Attribute Mapping");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"序号", "Attr Code", "Attr Name", "Target Attr Code"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (AttrInfoVo data : dataList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 1);
                row.createCell(1).setCellValue(data.getAttrCode());
                row.createCell(2).setCellValue(data.getAttrName());
                row.createCell(3).setCellValue(data.getTargetAttrCode());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}