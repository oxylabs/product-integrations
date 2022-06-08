module.exports = {
  Username: "",
  Password: "",
  Timeout: 5,
  RequestsRate: 10,
  RetriesNum: 3,
  UrlListName: "url_list_shared_dc.txt",
  ProxyListName: "proxy_list_shared_dc.txt",

  ProxyRegex: /^dc\.(?<country>\w{2})-?pr\.oxylabs\.io:\d+$/,
  DefaultProxyIndexName: "DEFAULT",
}
