    <?
    //where are we posting to?
    $user_name  = "root";
    $user_pass  = "123456";
    $host       = "localhost";
    $port       = "10035";
    $repository = "Hanoi";
    $query= "select ?label (sample(?lon)as ?lon)(sample(?lat)as ?lat)(sample(?uri)as ?uri)(sample(?num)as ?num)(sample(?street)as ?street)   (sample(?desc)as ?desc) (sample(?img)as ?img) {?uri rdfs:label ?label. filter(lang(?label)='en').?uri vtio:hasLongtitude ?lon.?uri vtio:hasLatitude ?lat. ?uri vtio:hasAbstract ?desc.?uri vtio:isWellKnown \"true\"^^xsd:boolean.?uri vtio:hasMedia ?media.?media vtio:hasURL ?img .  filter (regex(?img, \"png\", \"i\")||regex(?img,\"jpg\",\"i\")). optional{?uri vtio:hasLocation ?add.?add vtio:hasValue ?num.?add vtio:isPartOf ?str.?str rdfs:label ?street.filter(lang(?street)='en').}.  optional{?uri vtio:hasLocation ?str.?str rdfs:label ?street. filter(lang(?street)='en').}}group by ?label";
    $file = '/tmp/alg.log';
    $current = print_r($_REQUEST);
    file_put_contents($file, $current);

    $limit= 20;
    $url = 'http://'.$user_name.':'.$user_pass.'@'.$host.':'.$port.'/repositories/'.$repository;
    //what post fields?
    $fields = array(
        'query'   =>$query,
        'queryLn' =>'SPARQL',
        'limit'   =>$limit,
        'infer'   =>'false'
    );
 
    //build the urlencoded data
    $postvars='';
    $sep='';
    foreach($fields as $key=>$value) 
    { 
       $postvars.= $sep.urlencode($key).'='.urlencode($value); 
       $sep='&'; 
    }
 
    //open connection
    $ch = curl_init();
 
    //set the url, number of POST vars, POST data
    curl_setopt($ch,CURLOPT_URL,$url);
    curl_setopt($ch,CURLOPT_POST,count($fields));
    curl_setopt($ch,CURLOPT_POSTFIELDS,$postvars);
    curl_setopt($ch,CURLOPT_RETURNTRANSFER, TRUE);
    // execute post
    $result = curl_exec($ch);
    // close connection
 
    curl_close($ch);
 
    #$result = simplexml_load_string($result); 
    echo ($result);
?>
