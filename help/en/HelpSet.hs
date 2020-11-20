<?xml version='1.0' encoding='ISO-8859-1' ?>
<!DOCTYPE helpset
  PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 1.0//EN"
         "http://java.sun.com/products/javahelp/helpset_1_0.dtd">

<helpset version="1.0">
     <title>ADDA GUI Help</title>
     <maps>
          <mapref location="Map.jhm"/>
          <homeID>introduction</homeID>
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

     <presentation default=true>
        <name>main window</name>
        <size width="1024" height="700" />
        <location x="200" y="200" />
        <image>icon</image>
        <title>ADDA GUI Help</title>
        <toolbar>
           <helpaction>javax.help.BackAction</helpaction>
           <helpaction>javax.help.ForwardAction</helpaction>

        </toolbar>
     </presentation>
</helpset>
