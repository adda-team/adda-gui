<?xml version='1.0' encoding='ISO-8859-1' ?>
<!DOCTYPE helpset
  PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 1.0//EN"
         "http://java.sun.com/products/javahelp/helpset_1_0.dtd">

<helpset version="1.0">
     <title>ADDA GUI Help</title>
     <maps>
          <mapref location="Map.jhm"/>
          <homeID>overview</homeID>
     </maps>
     <view>
         <name>TOC</name>
         <label>Helper tree</label>
         <type>javax.help.TOCView</type>
         <data>TOC.xml</data>
     </view>
     <view>
          <name>Search</name>
          <label>Search</label>
          <type>javax.help.SearchView</type>
          <data engine="com.sun.java.help.search.DefaultSearchEngine">
            JavaHelpSearch
          </data>
     </view>
</helpset>
