/*var request = new XMLHttpRequest();

request.open('POST','http://sabernet.ddns.net:8080/api/login');

request.setRequestHeader('Authorization', 'Basic QWRtaW46cSVtIj1mSihWWSIjQnBMSDQ4fjspPkB1UWhnN2paNUA=');

request.send('matt');
*/
var request = new XMLHttpRequest();

request.open('GET','http://sabernet.ddns.net:8080/api/devices/9be0a1cf-d7d9-48f4-9122-26a8e295cae3/schedules');

request.setRequestHeader('Access-Control-Allow-Origin', '*');

request.send();

