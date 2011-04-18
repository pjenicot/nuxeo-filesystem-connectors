<html><head><title>vermeer RPC packet</title></head>
<body>
<p>method=getDocsMetaInfo:${request.version}
<#if includefiles>
<p>document_list=
<ul>
<#list docs as doc>
<ul>
<li>document_name=${doc.getRelativeFilePath(siteRoot)}
<li>meta_info=
<ul>
<li>Subject
<li>SW|
<li>vti_sourcecontrollockexpires
<li>TR|${doc.checkoutExpiryTS}
<li>vti_rtag
<li>SW|rt:${doc.etag}@00000000004
<li>vti_etag
<li>SW|&#34;&#123;${doc.etag}&#125;,3&#34;
<li>vti_parserversion
<li>SR|${config.WSSServerVersion}
<li>vti_timecreated
<li>TR|${doc.createdTS}
<li>_Category
<li>SW|
<li>vti_canmaybeedit
<li>BX|true
<li>vti_author
<li>SR|${doc.author}
<li>_Comments
<li>SW|
<li>vti_sourcecontrolcheckedoutby
<li>SR|${doc.checkoutUser}
<li>vti_sourcecontroltimecheckedout
<li>TR|${doc.checkoutTS}
<li>vti_sourcecontrolversion
<li>SR|V1.0
<li>vti_sourcecontrolcookie
<li>SR|fp_internal
<li>vti_approvallevel
<li>SR|
<li>vti_categories
<li>VW|
<li>vti_level
<li>IR|1
<li>vti_assignedto
<li>SR|
<li>Keywords
<li>SW|
<li>vti_modifiedby
<li>SR|${doc.lastModificator}
<li>vti_filesize
<li>IR|${doc.size}
<li>ContentTypeId
<li>SW|0x0101003C97D20C47E1C3498214C5297D0BA161
<li>vti_nexttolasttimemodified
<li>TR|${doc.modifiedTS}
<li>vti_title
<li>SR|
<li>_Author
<li>SW|${doc.author}
<li>vti_timelastmodified
<li>TR|${doc.modifiedTS}
<li>vti_sourcecontrolmultiuserchkoutby
<li>VR|${doc.checkoutUser}
<li>vti_candeleteversion
<li>BR|true
</ul>
</ul>
</#list>
</ul>
</#if>
<#if includefolders>
<p>urldirs=
<ul>
<#list folders as doc>
<ul>
<li>url=${doc.getRelativeFilePath(siteRoot)}
<li>meta_info=
<ul>
<li>vti_isexecutable
<li>BR|false
<li>vti_listenableminorversions
<li>BR|false
<li>vti_listenablemoderation
<li>BR|false
<li>vti_rtag
<li>SW|rt:${doc.etag}@00000000004
<li>vti_etag
<li>SW|&#34;&#123;${doc.etag}&#125;,0&#34;
<li>vti_isbrowsable
<li>BR|true
<li>vti_isscriptable
<li>BR|false
<li>vti_hassubdirs
<li>BR|true
<li>vti_listbasetype
<li>IR|1
<li>vti_timecreated
<li>TR|${doc.createdTS}
<li>vti_listservertemplate
<li>IR|101
<li>vti_listname
<li>SR|&#123;${doc.etag}&#125;
<li>vti_listtitle
<li>SR|${doc.title}
<li>vti_listenableversioning
<li>BR|false
<li>vti_canmaybeedit
<li>BX|true
<li>vti_candeleteversion
<li>BR|true
<li>vti_dirlateststamp
<li>TW|${doc.modifiedTS}
<li>vti_timelastmodified
<li>TR|${doc.modifiedTS}
<li>vti_listrequirecheckout
<li>BR|false
<li>vti_level
<li>IR|1
</ul>
</ul>
</#list>
</ul>
</#if>
<#if includeFailedUrls>
<p>failedUrls=
<#list failedUrls as url>
<ul>
<li>${url}
</ul>
</#list>
</#if>
</body>
</html>