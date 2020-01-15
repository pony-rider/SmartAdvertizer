/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core;

import com.mycompany.smartadvertizer.excelmapper.convert.Aliased;
import com.mycompany.smartadvertizer.excelmapper.mapping.ExcelType;
import com.mycompany.smartadvertizer.excelmapper.mapping.annotation.ColumnStyle;
import com.mycompany.smartadvertizer.excelmapper.mapping.annotation.ColumnMapping;
import com.mycompany.smartadvertizer.excelmapper.mapping.annotation.InputColumn;
import com.mycompany.smartadvertizer.excelmapper.mapping.annotation.OutputColumn;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 */

public class BillboardSide {

    private String gid;

    @ColumnMapping(input = @InputColumn(value = "E", nullable = false),
            output = @OutputColumn(header = "Адрес", style = @ColumnStyle(width = 40)))
    private String address;

    @ColumnMapping(input = @InputColumn(value = "D", nullable = false),
            output = @OutputColumn(header = "Сторона", style = @ColumnStyle(autoWidth = true)))
    private String side;

    @ColumnMapping(input = @InputColumn("F"), output = @OutputColumn(header = "Район"))
    private String region;

    @ColumnMapping(input = @InputColumn("G"), output = @OutputColumn(header = "Рейтинг"))
    private String rating;

    @ColumnMapping(input = @InputColumn("H"), output = @OutputColumn(header = "Рейтинг"))
    private Double longitude;

    @ColumnMapping(input = @InputColumn("I"), output = @OutputColumn(header = "Рейтинг"))
    private Double latitude;

    private String geoPoint;

    @ColumnMapping(input = @InputColumn("K"), output = @OutputColumn(header = "Освещение"))
    private Lighting lighting;

    @ColumnMapping(input = @InputColumn("L"), output = @OutputColumn(header = "Файл с фото"))
    private String photo;

    @ColumnMapping(input = @InputColumn("M"), output = @OutputColumn(header = "Файл с картой"))
    private String map;

    @ColumnMapping(input = @InputColumn("N"), output = @OutputColumn(header = "Фото",
            excelType = ExcelType.HYPERLINK, hyperlinkText = "фото"))
    private String photoLink;

    @ColumnMapping(input = @InputColumn("O"), output = @OutputColumn(header = "Карта",
            excelType = ExcelType.HYPERLINK, hyperlinkText = "карта"))
    private String mapLink;

    @ColumnMapping(input = @InputColumn("R"), output = @OutputColumn(header = "Тип конструкции"))
    private ConstuctionType constructionType;

    @ColumnMapping(input = @InputColumn("S"), output = @OutputColumn(header = "Размер"))
    private Size size;

    @ColumnMapping(input = @InputColumn("T"), output = 
            @OutputColumn(header = "Прайс", excelType = ExcelType.NUMERIC))
    private Double priceForAdAgency;

    @ColumnMapping(input = @InputColumn("U"), output = 
            @OutputColumn(header = "Стоимость для РА", excelType = ExcelType.NUMERIC))
    private Double discountedPriceForAdAgency;
    
    @ColumnMapping(input = @InputColumn("V"), output = 
            @OutputColumn(header = "Стоимость", excelType = ExcelType.NUMERIC))
    private Double price;

    @ColumnMapping(input = @InputColumn("W"), output = 
            @OutputColumn(header = "Монтаж", excelType = ExcelType.NUMERIC))
    private Double firstInstallationPrice;
    
    @ColumnMapping(input = @InputColumn("X"), output = 
            @OutputColumn(header = "Доп. монтаж", excelType = ExcelType.NUMERIC))
    private Double additionalInstallationPrice;
    
    @ColumnMapping(input = @InputColumn("Y"), output = @OutputColumn(header = "Материал"))
    private Material material;
    
    @ColumnMapping(input = @InputColumn("Z"), output = 
            @OutputColumn(header = "Печать", excelType = ExcelType.NUMERIC))
    private Double printingPrice;
    
    @ColumnMapping(input = @InputColumn("AA"), output = @OutputColumn(header = "Налог"))
    private String tax;
    
    private String description;
    
    @ColumnMapping(input = @InputColumn("AB"), output = @OutputColumn(
            header = "Разрешение", excelType = ExcelType.DATE))
    private LocalDate endOfLicence;
    
    private BookingStatus[] bookingStatuses;

    public static enum Lighting implements Aliased {

        ON("+"), OFF("-");
        private final String alias;

        private Lighting(String alias) {
            this.alias = alias;
        }

        @Override
        public String getAlias() {
            return alias;
        }
    }

    public static enum ConstuctionType implements Aliased {

        //STATIC, DYNAMIC;
        BILLBOARD("щит"), PRISMAVISION("призматрон");
        private final String alias;

        private ConstuctionType(String alias) {
            this.alias = alias;
        }

        @Override
        public String getAlias() {
            return alias;
        }
    }

    public static enum Size implements Aliased {

        STANDART("3х6"), DOUBLED("3х12");
        private final String alias;

        private Size(String alias) {
            this.alias = alias;
        }

        @Override
        public String getAlias() {
            return alias;
        }
    }

    public static enum Material implements Aliased {

        BANNER("баннер"), FILM("пленка");
        private final String alias;

        private Material(String alias) {
            this.alias = alias;
        }

        @Override
        public String getAlias() {
            return alias;
        }
    }

    
    
    public BillboardSide() {
    }

    public BillboardSide(String address, String side) {
        this.address = address;
        this.side = side;
    }

    /**
     * @return the gid
     */
    public String getGid() {
        return gid;
    }

    /**
     * @param gid the gid to set
     */
    public void setGid(String gid) {
        this.gid = gid;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the side
     */
    public String getSide() {
        return side;
    }

    /**
     * @param side the side to set
     */
    public void setSide(String side) {
        this.side = side;
    }

    /**
     * @return the rating
     */
    public String getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * @return the lighting
     */
    public Lighting getLighting() {
        return lighting;
    }

    /**
     * @param lighting the lighting to set
     */
    public void setLighting(Lighting lighting) {
        this.lighting = lighting;
    }

    /**
     * @return the photo
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * @param photo the photo to set
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * @return the map
     */
    public String getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(String map) {
        this.map = map;
    }

    /**
     * @return the photoLink
     */
    public String getPhotoLink() {
        return photoLink;
    }

    /**
     * @param photoLink the photoLink to set
     */
    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    /**
     * @return the mapLink
     */
    public String getMapLink() {
        return mapLink;
    }

    /**
     * @param mapLink the mapLink to set
     */
    public void setMapLink(String mapLink) {
        this.mapLink = mapLink;
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return the longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the geoPoint
     */
    public String getGeoPoint() {
        return geoPoint;
    }

    /**
     * @param geoPoint the geoPoint to set
     */
    public void setGeoPoint(String geoPoint) {
        this.geoPoint = geoPoint;
    }

    /**
     * @return the constructionType
     */
    public ConstuctionType getConstructionType() {
        return constructionType;
    }

    /**
     * @param constructionType the constructionType to set
     */
    public void setConstructionType(ConstuctionType constructionType) {
        this.constructionType = constructionType;
    }

    /**
     * @return the size
     */
    public Size getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(Size size) {
        this.size = size;
    }

    /**
     * @return the price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * @return the priceForAdAgency
     */
    public Double getPriceForAdAgency() {
        return priceForAdAgency;
    }

    /**
     * @param priceForAdAgency the priceForAdAgency to set
     */
    public void setPriceForAdAgency(Double priceForAdAgency) {
        this.priceForAdAgency = priceForAdAgency;
    }

    /**
     * @return the discountedPriceForAdAgency
     */
    public Double getDiscountedPriceForAdAgency() {
        return discountedPriceForAdAgency;
    }

    /**
     * @param discountedPriceForAdAgency the discountedPriceForAdAgency to set
     */
    public void setDiscountedPriceForAdAgency(Double discountedPriceForAdAgency) {
        this.discountedPriceForAdAgency = discountedPriceForAdAgency;
    }

    /**
     * @return the firstInstallationPrice
     */
    public Double getFirstInstallationPrice() {
        return firstInstallationPrice;
    }

    /**
     * @param firstInstallationPrice the firstInstallationPrice to set
     */
    public void setFirstInstallationPrice(Double firstInstallationPrice) {
        this.firstInstallationPrice = firstInstallationPrice;
    }

    /**
     * @return the additionalInstallationPrice
     */
    public Double getAdditionalInstallationPrice() {
        return additionalInstallationPrice;
    }

    /**
     * @param additionalInstallationPrice the additionalInstallationPrice to set
     */
    public void setAdditionalInstallationPrice(
            Double additionalInstallationPrice) {
        this.additionalInstallationPrice = additionalInstallationPrice;
    }

    /**
     * @return the material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * @param material the material to set
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * @return the printingPrice
     */
    public Double getPrintingPrice() {
        return printingPrice;
    }

    /**
     * @param printingPrice the printingPrice to set
     */
    public void setPrintingPrice(Double printingPrice) {
        this.printingPrice = printingPrice;
    }

    /**
     * @return the tax
     */
    public String getTax() {
        return tax;
    }

    /**
     * @param tax the tax to set
     */
    public void setTax(String tax) {
        this.tax = tax;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEndOfLicence() {
        return endOfLicence;
    }

    public void setEndOfLicence(LocalDate endOfLicence) {
        this.endOfLicence = endOfLicence;
    }

    /**
     * @return the bookingStatuses
     */
    public BookingStatus[] getBookingStatuses() {
        return bookingStatuses;
    }

    /**
     * @param bookingStatuses the bookingStatuses to set
     */
    public void setBookingStatuses(
            BookingStatus[] bookingStatuses) {
        this.bookingStatuses = bookingStatuses;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof BillboardSide) {
            BillboardSide side = (BillboardSide) obj;
            return Objects.equals(getAddress(), side.getAddress())
                    && Objects.equals(getSide(), side.getSide());
        }
        return false;
    }

    private boolean checkSideEquals(String side1, String side2) {
        return false;
    }

    @Override
    public int hashCode() {
        return getAddress().concat(getSide()).hashCode();
    }

    @Override
    public String toString() {
        return getAddress() + " " + getSide();
    }

}
