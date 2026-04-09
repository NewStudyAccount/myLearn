
package com.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.AttrInfoVo;

public class ValueChangeToExcel {

    public static void main(String[] args) {
//         List<AttrInfoVo> SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE = new ArrayList<>();
//         List<AttrInfoVo> SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE = new ArrayList<>();
//
//        //子节点中从服务信息中增加的属性
//         List<AttrInfoVo> SKY_SUB_SERVICE_PROPERTY_ATTR_CODE = new ArrayList<>();
//         List<AttrInfoVo> SKY_SUB_DISCNT_PROPERTY_ATTR_CODE = new ArrayList<>();
//
//
//        //SKY
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("deployment_method").attrName("Deployment Method").targetAttrCode("PM_DEPLOYMENT_METHOD").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("bandwidth_m_m").attrName("Download Bandwidth").targetAttrCode("PM_DOWNLOAD_BANDWIDTH").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("bandwidth_m_m").attrName("Upload Bandwidth").targetAttrCode("PM_UPLOAD_BANDWIDTH").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("intl_bandwidth_m_m").attrName("Intl Download Bandwidth").targetAttrCode("PM_INTL_DOWNLOAD_BANDWIDTH").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("intl_bandwidth_m_m").attrName("Intl Upload Bandwidth").targetAttrCode("PM_INTL_UPLOAD_BANDWIDTH").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("china_bandwidth_m_m").attrName("China Download Bandwidth").targetAttrCode("PM_CHI_DOWNLOAD_BANDWIDTH").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("china_bandwidth_m_m").attrName("China Upload Bandwidth").targetAttrCode("PM_CHI_UPLOAD_BANDWIDTH").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("noss_product_type").attrName("Product Type").targetAttrCode("PM_PRODUCT_TYPE").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("sub_order_type").attrName("Sub Product Type").targetAttrCode("PM_SUB_PRODUCT_TYPE").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("sub_order_type_cn").attrName("Sub Product Type").targetAttrCode("PM_SUB_PRODUCT_TYPE_CN").build());
//
////        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("ip_version").attrName("IP Version").targetAttrCode("PM_IP_VERSION").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("interface_type").attrName("Interface Type").targetAttrCode("PM_INTERFACE_TYPE").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("media_type").attrName("Media Type").targetAttrCode("PM_MEDIA_TYPE").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("routing_protocol").attrName("Routing Protocol").targetAttrCode("PM_ROUTING_PROTOCOL").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("line_type").attrName("Line Type").targetAttrCode("PM_LINE_TYPE").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("diversity").attrName("Diversity").targetAttrCode("PM_DIVERSITY").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("max_capactiy").attrName("Max Capacity").targetAttrCode("PM_MAX_CAPACITY").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("rack_id").attrName("Rack ID").targetAttrCode("PM_RACKID").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("room_id").attrName("Room ID").targetAttrCode("PM_ROOMID").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("site_code").attrName("Site Code").targetAttrCode("PM_SITE_CODE").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("pooling").attrName("Pooling").targetAttrCode("PM_POOLING").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("caged").attrName("Caged").targetAttrCode("PM_CAGED").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("correlated_rack_s_nf_number").attrName("Correlated Rack's NF Number").targetAttrCode("PM_CORR_RACK_NF_NUMBER").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("standard_rack").attrName("Standard rack").targetAttrCode("PM_STANDARD_RACK").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("standard_rack_other").attrName("Temp Standard rack").targetAttrCode("TEMP_PM_STANDARD_RACK").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("hkt_customer_ip").attrName("HKT Announce Customer Own IP").targetAttrCode("PM_HKT_ANNOUNCE_CUSTOMER_OWN_IP").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("Circuit_Remark").attrName("Circuit Remark").targetAttrCode("PM_CIRCUIT_REMARKS").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("Diversity_Service_no").attrName("Diversity Service Number").targetAttrCode("PM_DIVERSITY_SERVICE_NUMBER").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("Pre-study").attrName("Pre-Study No").targetAttrCode("PM_PRE_STUDY_NUMBER").build());
//        SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("Resilience_Service_no").attrName("Resilience Service Number").targetAttrCode("PM_RESILIENCE_SERVICE_NUMBER").build());
//
//
//
//        SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("ip_version").attrName("IP Version").targetAttrCode("PM_IP_VERSION").build());
//        SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("level").attrName("Level").targetAttrCode("PM_LEVEL").build());
//        //BCP
//        SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("correlated_rack_s_nf_number").attrName("Correlated Rack's NF Number").targetAttrCode("PM_CORR_RACK_NF_NUMBER").build());
//        SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("room_no").attrName("Room No").targetAttrCode("PM_ROOM_NUMBER").build());
//        SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("location").attrName("Location").targetAttrCode("PM_LOCATION").build());
//        SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("seat").attrName("Seat").targetAttrCode("PM_SEAT_NUMBER").build());
//        SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("no_of_seats").attrName("No. of Seats").targetAttrCode("PM_NO_OF_SEATS").build());
//        // Customer Rack to Customer Rack/MMR Customer Rack to Customer Rack
//        SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("a_end").attrName("A End").targetAttrCode("PM_AEND_CONNECTOR").build());
//        SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("a_end_fibre_connector").attrName("A End Connector").targetAttrCode("PM_AEND_FIBRE_CONNECTOR").build());
//        SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("z_end").attrName("Z End").targetAttrCode("PM_ZEND_CONNECTOR").build());
//        SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("z_end_fibre_connector").attrName("Z End Connector").targetAttrCode("PM_FIBRE_CONNECTOR_END:ZEnd").build());
//        SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("fibre_mode").attrName("Fibre Mode").targetAttrCode("PM_FIBRE_MODE").build());
//        SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("cicuit_no").attrName("Circuit No").targetAttrCode("PM_CIRCUIT_NUMBER").build());
//        SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("service_provider").attrName("Service Provider").targetAttrCode("PM_SERVICE_PROVVIDER").build());
//
//        SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("no_of_lan_ip_address").attrName("No of LAN IP").targetAttrCode("PM_NO_OF_LAN_IP").build());
//        SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("hkt_customer_ip").attrName("HKT Announce Customer Own IP").targetAttrCode("PM_HKT_ANNOUNCE_CUSTOMER_OWN_IP").build());
//
//        SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("automatic_transfer_switch").attrName("Automatic Transfer Switch").targetAttrCode("PM_AUTOMATIC_TRANSFER_SWITCH").build());
//        SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("backup_tape_rotation").attrName("Backup Tape Rotation").targetAttrCode("PM_BACKUP_TAPE_ROTATION").build());
//        SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("change_of_power_bar_socket_type").attrName("Change of Power Bar Socket Type").targetAttrCode("PM_CHANGE_OF_POWER_BAR_SOCKET_TYPE").build());
//
//
//        // 子节点
//        // Router
//        SKY_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("router_model").attrName("Model").targetAttrCode("PM_MODEL").build());
//        SKY_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("item_code").attrName("Item Code").targetAttrCode("PM_ITEM_CODE").build());
//        SKY_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("router_management_service_rms").attrName("RMS").targetAttrCode("PM_RMS").build());
//
//


//        exportToExcel(SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE, "SKY_Main_DISCNT_Property_Attr.xlsx");
//        exportToExcel(SKY_MAIN_SERVICE_PROPERTY_ATTR_CODE, "SKY_Main_Service_Property_Attr.xlsx");
//        exportToExcel(SKY_SUB_DISCNT_PROPERTY_ATTR_CODE, "SKY_SUB_DISCNT_Property_Attr.xlsx");
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