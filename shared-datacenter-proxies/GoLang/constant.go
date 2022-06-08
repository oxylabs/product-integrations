package main

const Username = ""
const Password = ""
const Timeout = 5
const RequestsRate = 10
const RetriesNum = 3
const UrlListName = "url_list_shared_dc.txt"
const ProxyListName = "proxy_list_shared_dc.txt"

const ProxyRegex = `^dc\.(?P<country>\w{2})-?pr\.oxylabs\.io:\d+$`
const DefaultProxyIndexName = "DEFAULT"

