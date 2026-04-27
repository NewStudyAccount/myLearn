
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
         List<AttrInfoVo> CLOUD_MAIN_SERVICE_PROPERTY_ATTR_CODE = new ArrayList<>();
         List<AttrInfoVo> SKY_MAIN_DISCNT_PROPERTY_ATTR_CODE = new ArrayList<>();

        //子节点中从服务信息中增加的属性
         List<AttrInfoVo> CLOUD_SUB_SERVICE_PROPERTY_ATTR_CODE = new ArrayList<>();
         List<AttrInfoVo> CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE = new ArrayList<>();



        CLOUD_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("public_cloud_vendor_611").attrName("Cloud Vendor").targetAttrCode("PM_CLOUD_TYPE").build());
        CLOUD_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("managed_service_tier_611").attrName("Managed Service Tier").targetAttrCode("PM_CLOUD_TIER").build());
        CLOUD_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("premium_storage_611").attrName("Premium Storage").targetAttrCode("PM_CLOUD_PREMIUM_STORAGE").build());

        //VM Ware
        CLOUD_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("cpu_611").attrName("CPU").targetAttrCode("PM_CLOUD_CPU").build());
        CLOUD_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("ram_611").attrName("RAM").targetAttrCode("PM_CLOUD_RAM").build());
        CLOUD_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("storage_611").attrName("Storage").targetAttrCode("PM_CLOUD_STORAGE").build());
        CLOUD_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("mpc_storage_611").attrName("Storage").targetAttrCode("PM_CLOUD_STORAGE").build());
        CLOUD_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("brand_611").attrName("Brand").targetAttrCode("PM_CLOUD_FW_BRAND").build());


        CLOUD_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("private_vlan_611").attrName("Private Vlan").targetAttrCode("PM_CLOUD_PRIVATE_VLAN").build());
        CLOUD_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("virtual_firewall_611").attrName("Virtual Firewall").targetAttrCode("PM_CLOUD_FIREWALL_VFW").build());
        CLOUD_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("portal_user_611").attrName("Portal User").targetAttrCode("PM_CLOUD_PORTAL_USER").build());
        CLOUD_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("cloudview_611").attrName("CloudView").targetAttrCode("PM_CLOUD_CLOUD_VIEW").build());
        CLOUD_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("product_type_611").attrName("Product Type").targetAttrCode("PM_PRODUCT_TYPE").build());
        CLOUD_MAIN_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("extra").attrName("Cloud Extra").targetAttrCode("PM_CLOUD_MPC_EXTRA").build());




        CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("storage_611").attrName("STORAGE").targetAttrCode("PM_CLOUD_STORAGE").build());
        CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("cpu_611").attrName("CPU").targetAttrCode("PM_CLOUD_CPU").build());
        CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("ram_611").attrName("RAM").targetAttrCode("PM_CLOUD_RAM").build());
        CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("premium_storage_611").attrName("Premium Storage").targetAttrCode("PM_CLOUD_PREMIUM_STORAGE").build());
        CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("qty").attrName("Quantity").targetAttrCode("PM_QUANTITY").build());

        CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("bandwidth_611").attrName("Cloud Connect(IPv4) Bandwidth").targetAttrCode("PM_CLOUD_CLOUD_CONNECT_BANDWIDTH").build());
        CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("bandwidth_32").attrName("Premium Cloud Connect(IPv4) Bandwidth").targetAttrCode("PM_CLOUD_PREMIUM_CLOUD_CONNECT_BANDWIDTH").build());
        CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("anti_ddos_611").attrName("Premium Cloud Connect(IPv4) Anti-DDoS").targetAttrCode("PM_CLOUD_PREMIUM_CLOUD_CONNECT_ANTI_DDOS").build());
        CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("bandwidth_33").attrName("Metro-IP Connect Bandwidth").targetAttrCode("PM_CLOUD_MIP_CONNECT_BANDWIDTH").build());
        CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("bandwidth_34").attrName("IPVPN Connect Bandwidth").targetAttrCode("PM_CLOUD_IPVPN_CONNECT_BANDWIDTH").build());
        CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("bandwidth_35").attrName("CloudDirect Attach Bandwidth").targetAttrCode("PM_CLOUD_CLOUD_CIRECT_ATTACH_BANDWIDTH").build());
        CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("private_network_service_611").attrName("CloudDirect Attach Private Network Service Usage").targetAttrCode("PM_CLOUD_CLOUD_CIRECT_ATTACH_PRIVATE_NETWORK_SERVICE").build());
        CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("bandwidth_37").attrName("Equipment Hosting Bandwidth").targetAttrCode("PM_CLOUD_EQUIPMENT_HOSTING_BANDWIDTH").build());

        CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("size_611").attrName("Automated Snapshot Function Size").targetAttrCode("PM_CLOUD_AUTOMATED_SNAPSHOT_CAPACITY").build());

        CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("version_611").attrName("Microsoft SPLA - SQL Server Version").targetAttrCode("PM_CLOUD_MS_SPLA_SQL_SERVER_VERSION").build());
        CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("version_1").attrName("Microsoft SPLA - Office Version").targetAttrCode("PM_CLOUD_MS_SPLA_OFFICE_VERSION").build());
        CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("unit_611").attrName("CloudWAF-100").targetAttrCode("PM_CLOUD_CLOUD_WAF_100").build());
        CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("unit_two").attrName("CloudWAF-200").targetAttrCode("PM_CLOUD_CLOUD_WAF_200").build());


        CLOUD_SUB_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("location_611").attrName("PM_SKYEXCHANGE_LOCATION").targetAttrCode("PM_CLOUD_CLOUD_VIEW").build());
        CLOUD_SUB_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("cpu_611").attrName("CPU").targetAttrCode("PM_CLOUD_CPU").build());
        CLOUD_SUB_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("ram_611").attrName("RAM").targetAttrCode("PM_CLOUD_RAM").build());
        CLOUD_SUB_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("storage_611").attrName("Storage").targetAttrCode("PM_CLOUD_STORAGE").build());
        CLOUD_SUB_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("cloudview_611").attrName("CloudView").targetAttrCode("PM_CLOUD_CLOUD_VIEW").build());
        CLOUD_SUB_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("premium_storage_611").attrName("Premium Storage").targetAttrCode("PM_CLOUD_PREMIUM_STORAGE").build());
        CLOUD_SUB_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("quantity_1").attrName("Bulk Storage 100GB").targetAttrCode("PM_CLOUD_BULK_STORAGE_100GB").build());
        CLOUD_SUB_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("quantity_2").attrName("Bulk Storage 500GB").targetAttrCode("PM_CLOUD_BULK_STORAGE_500GB").build());
        CLOUD_SUB_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("quantity_3").attrName("Bulk Storage 1TB").targetAttrCode("PM_CLOUD_BULK_STORAGE_1TB").build());
        CLOUD_SUB_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("quantity_4").attrName("Bulk Storage 5TB").targetAttrCode("PM_CLOUD_BULK_STORAGE_5TB").build());
        CLOUD_SUB_SERVICE_PROPERTY_ATTR_CODE.add(AttrInfoVo.builder().attrCode("quantity_5").attrName("Bulk Storage 10TB").targetAttrCode("PM_CLOUD_BULK_STORAGE_10TB").build());

        exportToExcel(CLOUD_MAIN_SERVICE_PROPERTY_ATTR_CODE, "CLOUD_MAIN_SERVICE_Property_Attr.xlsx");
        exportToExcel(CLOUD_SUB_DISCNT_PROPERTY_ATTR_CODE, "CLOUD_SUB_DISCNT_Property_Attr.xlsx");
        exportToExcel(CLOUD_SUB_SERVICE_PROPERTY_ATTR_CODE, "CLOUD_SUB_SERVICE_Property_Attr.xlsx");
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