import urllib.request

username = 'USERNAME'
password = 'PASSWORD'
ip = 'PROXY_IP'
port = 'PROXY_PORT'

entry = ('http://%s:%s@%s:%s' %
    (username, password, ip, port))
query = urllib.request.ProxyHandler({
    'http': entry,
    'https': entry,
})

execute = urllib.request.build_opener(query)
print(execute.open('https://ip.oxylabs.io').read())