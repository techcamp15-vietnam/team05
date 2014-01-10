        <?
    //where are we posting to?
    $user_name  = "root";
    $user_pass  = "123456";
    $host       = "localhost";
    $port       = "10035";
    $repository = "Hanoi";
    $request=$_POST['request'];
    $lon=$_POST['lon'];
    $lat=$_POST['lat'];
    $key=$_POST['keyword'];
    $cat=$_POST['cat'];
    if($cat=="Famous")$request="getFamousPlaces";
    if($cat=="Restaurant")$request="getRestaurant";
    if($cat=="Bar")$request="getBar";
    $rad=0.8;
 // $lat=21.004414;
 //    $lon=105.845908;
 //    // $key="ha noi";
    //$cat="ATM";
    $query=array();
//ok
    $query["getFamousPlaces"]=    "select ?label (sample(?lon)as ?lon)(sample(?lat)as ?lat)(sample(?cat)as ?cat)(sample(?num)as ?num)(sample(?street)as ?street)(sample(?desc)as ?desc) (sample(?img)as ?img)(sample(?phone)as ?phone)(sample(?uri)as ?uri) {?uri rdfs:label ?label.     filter(lang(?label)='en').    ?uri rdf:type ?cat .filter(!strstarts(str(?cat),str(owl:))).    ?uri vtio:hasLongtitude ?lon.?uri vtio:hasLatitude ?lat.     ?uri vtio:hasAbstract ?desc.?uri vtio:isWellKnown \"true\"^^xsd:boolean.    ?uri vtio:hasMedia ?media.?media vtio:hasURL ?img .      filter (regex(?img, \"png\", \"i\")||regex(?img,\"jpg\",\"i\")).     optional{?uri vtio:hasLocation     ?add.?add vtio:hasValue ?num.    ?add vtio:isPartOf ?str.    ?str rdfs:label ?street.filter(lang(?street)='en').}.      optional{?uri vtio:hasLocation ?str.?str rdfs:label ?street. filter(lang(?street)='en').}. optional{?uri vtio:hasContact ?contact.  ?contact vtio:hasPhoneNumber ?phone.}}group by ?label";
    //not ok
    $query["getNearbyPlaces"]=    "select ?label (sample(?lon)as ?lon)(sample(?lat)as ?lat)(sample(?cat)as ?cat)(sample(?num)as ?num)(sample(?street)as ?street)(sample(?desc)as ?desc) (sample(?img)as ?img)(sample(?phone)as ?phone)(sample(?uri)as ?uri) { GEO OBJECT SUBTYPE 'http://franz.com/ns/allegrograph/3.0/geospatial/spherical/degrees/-180.0/180.0/-90.0/90.0/5.0'  HAVERSINE (POINT(".$lon.",".$lat."), ".$rad."KM) { ?uri vtio:hasGeoPoint ?loc. ?uri rdfs:label ?label. ?uri vtio:hasLatitude ?lat.?uri vtio:hasLongtitude ?lon.} where { ?uri rdf:type ?cat . filter(!strstarts(str(?cat),str(owl:))). ?uri vtio:hasAbstract ?desc.   ?uri vtio:hasMedia ?media.?media vtio:hasURL ?img .  filter (regex(?img, \"png\", \"i\")||regex(?img,\"jpg\",\"i\")).     optional{?uri vtio:hasLocation     ?add. ?add vtio:hasValue ?num.    ?add vtio:isPartOf ?str.    ?str rdfs:label ?street. filter(lang(?street)='en').}.   optional{?uri vtio:hasLocation ?str.?str rdfs:label ?street. filter(lang(?street)='en'). } }filter(lang(?label)='en'). optional{?uri vtio:hasContact ?contact.  ?contact vtio:hasPhoneNumber ?phone.} }group by ?label  " ;
    //$query["getNearbyPlaces"]=    "select ?label (sample(?lon)as ?lon)(sample(?lat)as ?lat)(sample(?cat)as ?cat)(sample(?num)as ?num)(sample(?street)as ?street)(sample(?desc)as ?desc) (sample(?img)as ?img)(sample(?phone)as ?phone)(sample(?uri)as ?uri) { GEO OBJECT SUBTYPE 'http://franz.com/ns/allegrograph/3.0/geospatial/spherical/degrees/-180.0/180.0/-90.0/90.0/5.0'  HAVERSINE (POINT(".$lon.",".$lat."), ".$rad."KM) { ?uri vtio:hasGeoPoint ?loc. ?uri rdfs:label ?label. ?uri vtio:hasLatitude ?lat.?uri vtio:hasLongtitude ?lon.} where { ?uri rdf:type ?cat . filter(!strstarts(str(?cat),str(owl:))). ?uri vtio:hasAbstract ?desc.   ?uri vtio:hasMedia ?media.?media vtio:hasURL ?img .  filter (regex(?img, \"png\", \"i\")||regex(?img,\"jpg\",\"i\")).     optional{?uri vtio:hasLocation     ?add. ?add vtio:hasValue ?num.    ?add vtio:isPartOf ?str.    ?str rdfs:label ?street. filter(lang(?street)='en').}.   optional{?uri vtio:hasLocation ?str.?str rdfs:label ?street. filter(lang(?street)='en'). } }filter(lang(?label)='en'). optional{?uri vtio:hasContact ?contact.  ?contact vtio:hasPhoneNumber ?phone.} }group by ?label ORDER BY geo:haversine-km(?loc,(POINT(".$lon.",".$lat.")) ) " ;
    //ok
    $query["getPlacesByKeyword"]= "select ?label (sample(?cat)as ?cat) (sample(?lon)as ?lon)(sample(?lat)as ?lat)(sample(?num)as ?num)(sample(?street)as ?street)(sample(?desc)as ?desc) (sample(?img)as ?img)(sample(?phone)as ?phone)(sample(?uri)as ?uri){GEO OBJECT SUBTYPE 'http://franz.com/ns/allegrograph/3.0/geospatial/spherical/degrees/-180.0/180.0/-90.0/90.0/5.0' HAVERSINE (POINT(".$lon.",".$lat."),".$rad."KM)  {?uri vtio:hasGeoPoint ?loc. ?uri rdfs:label ?label.  ?uri vtio:hasLatitude ?lat. ?uri vtio:hasLongtitude ?lon.}  where {   filter regex(?label,\"".$key."\", \"i\"). ?uri rdfs:label ?label.  ?uri rdf:type ?cat .filter(!strstarts(str(?cat),str(owl:))). ?uri vtio:hasLongtitude ?lon. ?uri vtio:hasLatitude ?lat.filter(lang(?label)='en').  optional{?uri vtio:hasAbstract ?desc.}. optional{?uri vtio:hasLocation ?add.?add vtio:hasValue ?num. ?add vtio:isPartOf ?str.?str rdfs:label ?street. filter(lang(?street)='en').}. optional{?uri vtio:hasLocation ?str.?str rdfs:label ?street. filter(lang(?street)='vn').}. optional{?uri vtio:hasMedia ?media.?media vtio:hasURL ?img . filter (regex(?img, \"png\", \"i\")||regex(?img,\"jpg\",\"i\")).}.  optional{?uri vtio:hasContact ?contact.?contact vtio:hasPhoneNumber ?phone.}  }filter(lang(?label)='en').} group by ?label";
    //ok
    $query["getPlacesByCat"]=     "select ?label (sample(?cat)as ?cat) (sample(?lon)as ?lon)(sample(?lat)as ?lat)(sample(?num)as ?num)(sample(?street)as ?street)(sample(?desc)as ?desc) (sample(?img)as ?img)(sample(?phone)as ?phone)(sample(?uri)as ?uri){GEO OBJECT SUBTYPE 'http://franz.com/ns/allegrograph/3.0/geospatial/spherical/degrees/-180.0/180.0/-90.0/90.0/5.0' HAVERSINE (POINT(".$lon.",".$lat."),".$rad."KM)  {?uri vtio:hasGeoPoint ?loc. ?uri rdfs:label ?label.  ?uri vtio:hasLatitude ?lat.?uri vtio:hasLongtitude ?lon.}  where {  ?uri rdf:type ?cat .  filter(?cat= vtio:".$cat."). ?uri vtio:hasLongtitude ?lon. ?uri vtio:hasLatitude ?lat.  optional{?uri vtio:hasAbstract ?desc.} optional{  ?uri vtio:hasMedia ?media.?media vtio:hasURL ?img .filter (regex(?img, \"png\", \"i\")||regex(?img,\"jpg\",\"i\")).}. optional{?uri vtio:hasLocation     ?add. ?add vtio:hasValue ?num.   ?add vtio:isPartOf ?str.  ?str rdfs:label ?street. filter(lang(?street)='en').}. optional{?uri vtio:hasLocation ?str.?str rdfs:label ?street.  filter(lang(?street)='en')}.  optional{?uri vtio:hasContact ?contact.?contact vtio:hasPhoneNumber ?phone.}}filter(lang(?label)='en').}group by ?label";
    $query["getRestaurant"]=      "select ?label (sample(?cat)as ?cat) (sample(?lon)as ?lon)(sample(?lat)as ?lat)(sample(?num)as ?num)(sample(?street)as ?street)(sample(?desc)as ?desc) (sample(?img)as ?img)(sample(?phone)as ?phone)(sample(?uri)as ?uri){GEO OBJECT SUBTYPE 'http://franz.com/ns/allegrograph/3.0/geospatial/spherical/degrees/-180.0/180.0/-90.0/90.0/5.0' HAVERSINE (POINT(".$lon.",".$lat."),".$rad."KM)  {?uri vtio:hasGeoPoint ?loc. ?uri rdfs:label ?label.  ?uri vtio:hasLatitude ?lat.?uri vtio:hasLongtitude ?lon.}  where {  ?uri rdf:type ?cat .  filter(?cat in(vtio:KFC,vtio:Fast-Food,vtio:Restaurant,vtio:Popular-Restaurant,vtio:Luxurious-Restaurant)).?uri vtio:hasLongtitude ?lon. ?uri vtio:hasLatitude ?lat.  optional{?uri vtio:hasAbstract ?desc.} optional{  ?uri vtio:hasMedia ?media.?media vtio:hasURL ?img .filter (regex(?img, \"png\", \"i\")||regex(?img,\"jpg\",\"i\")).}. optional{?uri vtio:hasLocation     ?add. ?add vtio:hasValue ?num.   ?add vtio:isPartOf ?str.  ?str rdfs:label ?street. filter(lang(?street)='en').}. optional{?uri vtio:hasLocation ?str.?str rdfs:label ?street.  filter(lang(?street)='en')}. optional{?uri vtio:hasContact ?contact.?contact vtio:hasPhoneNumber ?phone.}}filter(lang(?label)='en').}group by ?label";
    $query["getBar"]=             "select ?label (sample(?cat)as ?cat) (sample(?lon)as ?lon)(sample(?lat)as ?lat)(sample(?num)as ?num)(sample(?street)as ?street)(sample(?desc)as ?desc) (sample(?img)as ?img)(sample(?phone)as ?phone)(sample(?uri)as ?uri){GEO OBJECT SUBTYPE 'http://franz.com/ns/allegrograph/3.0/geospatial/spherical/degrees/-180.0/180.0/-90.0/90.0/5.0' HAVERSINE (POINT(".$lon.",".$lat."),".$rad."KM)  {?uri vtio:hasGeoPoint ?loc. ?uri rdfs:label ?label.  ?uri vtio:hasLatitude ?lat.?uri vtio:hasLongtitude ?lon.}  where {  ?uri rdf:type ?cat .  filter(?cat in(vtio:Bar,vtio:Karaoke)).?uri vtio:hasLongtitude ?lon. ?uri vtio:hasLatitude ?lat.  optional{?uri vtio:hasAbstract ?desc.} optional{  ?uri vtio:hasMedia ?media.?media vtio:hasURL ?img .filter (regex(?img, \"png\", \"i\")||regex(?img,\"jpg\",\"i\")).}. optional{?uri vtio:hasLocation     ?add. ?add vtio:hasValue ?num.   ?add vtio:isPartOf ?str.  ?str rdfs:label ?street. filter(lang(?street)='en').}. optional{?uri vtio:hasLocation ?str.?str rdfs:label ?street.  filter(lang(?street)='en')}. optional{?uri vtio:hasContact ?contact.?contact vtio:hasPhoneNumber ?phone.}}filter(lang(?label)='en').}group by ?label";
    //
    $query["getNearPlace"]=      "select ?label (sample(?lon)as ?lon)(sample(?lat)as ?lat)(sample(?cat)as ?cat)(sample(?num)as ?num)(sample(?street)as ?street)(sample(?desc)as ?desc) (sample(?img)as ?img)(sample(?phone)as ?phone)(sample(?uri)as ?uri) { GEO OBJECT SUBTYPE 'http://franz.com/ns/allegrograph/3.0/geospatial/spherical/degrees/-180.0/180.0/-90.0/90.0/5.0'  HAVERSINE (POINT(".$lon.",".$lat."), ".$rad."KM) { ?uri vtio:hasGeoPoint ?loc. ?uri rdfs:label ?label. ?uri vtio:hasLatitude ?lat.?uri vtio:hasLongtitude ?lon.} where { ?uri rdf:type ?cat . filter(!strstarts(str(?cat),str(owl:))). ?uri vtio:hasAbstract ?desc.   ?uri vtio:hasMedia ?media.?media vtio:hasURL ?img .  filter (regex(?img, \"png\", \"i\")||regex(?img,\"jpg\",\"i\")).     optional{?uri vtio:hasLocation     ?add. ?add vtio:hasValue ?num.    ?add vtio:isPartOf ?str.    ?str rdfs:label ?street. filter(lang(?street)='en').}.   optional{?uri vtio:hasLocation ?str.?str rdfs:label ?street. filter(lang(?street)='en'). } }filter(lang(?label)='en'). optional{?uri vtio:hasContact ?contact.  ?contact vtio:hasPhoneNumber ?phone.} }group by ?label " ;
     //$test="select ?label (sample(?lon)as ?lon)(sample(?lat)as ?lat)(sample(?cat)as ?cat)(sample(?num)as ?num)(sample(?street)as ?street)(sample(?desc)as ?desc) (sample(?img)as ?img) {?uri rdfs:label ?label.     filter(lang(?label)='en').    ?uri rdf:type ?cat . filter(!strstarts(str(?cat),str(owl:))).    ?uri vtio:hasLongtitude ?lon.?uri vtio:hasLatitude ?lat.     ?uri vtio:hasAbstract ?desc.?uri vtio:isWellKnown \"true\"^^xsd:boolean.    ?uri vtio:hasMedia ?media.?media vtio:hasURL ?img .      filter (regex(?img, \"png\", \"i\")||regex(?img,\"jpg\",\"i\")).     optional{?uri vtio:hasLocation     ?add.?add vtio:hasValue ?num.    ?add vtio:isPartOf ?str.    ?str rdfs:label ?street.filter(lang(?street)='en').}.      optional{?uri vtio:hasLocation ?str.?str rdfs:label ?street. filter(lang(?street)='en').}}group by ?label";
    $limit=25;
    $url = 'http://'.$user_name.':'.$user_pass.'@'.$host.':'.$port.'/repositories/'.$repository;
    //what post fields?
    $fields = array(
        'query'   =>$query[$request],
        'queryLn' =>'SPARQL',
        'limit'   =>$limit,
        'infer'   =>'false'
    );
 //echo $keyword;
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
//print_r($result);
    $data="<place>";
    $result=XMLtoArray($result);
    //print_r($result);
  
    $result=$result['SPARQL'];
    if(count($result)<4)
    {   
        $data=$data." </place>";
        echo $data;
        return;
    }
    $result=$result['RESULTS'];

    $check=array();
    $check=initArray($check);
    // print_r($cars);
    $index=1;
    foreach ($result as $key =>$value) {
        if(count($value)>1)
        {
        //print_r($value);
      foreach ($value as $key=>$val) {

            foreach ($val as $key=>$place) {
            $data=$data."<result>";
             foreach ($place as $key=>$place1) {  
            //print_r($place1);
            $name=$place1['NAME'];
            
             if (array_key_exists('URI', $place1))
                {
                    $detail=$place1['URI'];
                    $data=$data."<".$name.">".$detail."</".$name.">";
                    $check[$name]=1;
                }
             else {
                $content=$place1['LITERAL'];
           //print_r($place1);    
                      foreach ($content as $key =>$content1) {
                  $detail=$content1['content'];
            
                 //print_r($detail);
                 //echo "==============================";

                if($name=="desc")
                    $detail= nomalizeString($detail);                
                 $data=$data."<".$name.">".$detail."</".$name.">";
                 $check[$name]=1;
            }
        }
      }  
   }

    foreach ($check as $key => $val2) {
        if($val2==0)$data=$data."<".$key."> </".$key.">";
    }
      $check=initArray($check);
        $data=$data."</result>"; 
        $index=1; 
  }
}
else
{
            $val=$value;
            foreach ($val as $key=>$place) {
            $data=$data."<result>";
             foreach ($place as $key=>$place1) {  
            //print_r($place1);
            $name=$place1['NAME'];
            
             if (array_key_exists('URI', $place1))
                {
                    $detail=$place1['URI'];
                    $data=$data."<".$name.">".$detail."</".$name.">";
                    $check[$name]=1;
                }
             else {
                $content=$place1['LITERAL'];
           //print_r($place1);    
                      foreach ($content as $key =>$content1) {
                  $detail=$content1['content'];
            
                 //print_r($detail);
                 //echo "==============================";
                 $data=$data."<".$name.">".$detail."</".$name.">";
                 $check[$name]=1;
            }
        }
      }  
       foreach ($check as $key => $val2) {
        if($val2==0)$data=$data."<".$key."> </".$key.">";
    }
      $check=initArray($check);
        $data=$data."</result>"; 
        $index=1; 
   }

}
}
$data=$data."</place>";
echo $data;
return ;
function nomalizeString($str)
{
    $source= array("&", "<", ">", "\"", "'", "{", "}", "@");
    //$source= array("<", ">",);
    $replace = array(" ", " ", " ", " ", " ", " ");
    $newstring = str_replace( $source ,$replace, $str);
    return $newstring;
}
function initArray($arr)
{
   $arr['label']=0;
   $arr['lon']=0;
   $arr['lat']=0;
   $arr['cat']=0;
   $arr['num']=0;
   $arr['street']=0;
   $arr['desc']=0;
    $arr['img']=0;
    $arr['phone']=0;
    $arr['uri']=0;
    return $arr;
}
function XMLtoArray($XML)
{
    $xml_parser = xml_parser_create();
    xml_parse_into_struct($xml_parser, $XML, $vals);
    xml_parser_free($xml_parser);
    // wyznaczamy tablice z powtarzajacymi sie tagami na tym samym poziomie
    $_tmp='';
    foreach ($vals as $xml_elem) {
        $x_tag=$xml_elem['tag'];
        $x_level=$xml_elem['level'];
        $x_type=$xml_elem['type'];
        if ($x_level!=1 && $x_type == 'close') {
            if (isset($multi_key[$x_tag][$x_level]))
                $multi_key[$x_tag][$x_level]=1;
            else
                $multi_key[$x_tag][$x_level]=0;
        }
        if ($x_level!=1 && $x_type == 'complete') {
            if ($_tmp==$x_tag)
                $multi_key[$x_tag][$x_level]=1;
            $_tmp=$x_tag;
        }
    }
    // jedziemy po tablicy
    foreach ($vals as $xml_elem) {
        $x_tag=$xml_elem['tag'];
        $x_level=$xml_elem['level'];
        $x_type=$xml_elem['type'];
        if ($x_type == 'open')
            $level[$x_level] = $x_tag;
        $start_level = 1;
        $php_stmt = '$xml_array';
        if ($x_type=='close' && $x_level!=1)
            $multi_key[$x_tag][$x_level]++;
        while ($start_level < $x_level) {
            $php_stmt .= '[$level['.$start_level.']]';
            if (isset($multi_key[$level[$start_level]][$start_level]) && $multi_key[$level[$start_level]][$start_level])
                $php_stmt .= '['.($multi_key[$level[$start_level]][$start_level]-1).']';
            $start_level++;
        }
        $add='';
        if (isset($multi_key[$x_tag][$x_level]) && $multi_key[$x_tag][$x_level] && ($x_type=='open' || $x_type=='complete')) {
            if (!isset($multi_key2[$x_tag][$x_level]))
                $multi_key2[$x_tag][$x_level]=0;
            else
                $multi_key2[$x_tag][$x_level]++;
            $add='['.$multi_key2[$x_tag][$x_level].']';
        }
        if (isset($xml_elem['value']) && trim($xml_elem['value'])!='' && !array_key_exists('attributes', $xml_elem)) {
            if ($x_type == 'open')
                $php_stmt_main=$php_stmt.'[$x_type]'.$add.'[\'content\'] = $xml_elem[\'value\'];';
            else
                $php_stmt_main=$php_stmt.'[$x_tag]'.$add.' = $xml_elem[\'value\'];';
            eval($php_stmt_main);
        }
        if (array_key_exists('attributes', $xml_elem)) {
            if (isset($xml_elem['value'])) {
                $php_stmt_main=$php_stmt.'[$x_tag]'.$add.'[\'content\'] = $xml_elem[\'value\'];';
                eval($php_stmt_main);
            }
            foreach ($xml_elem['attributes'] as $key=>$value) {
                $php_stmt_att=$php_stmt.'[$x_tag]'.$add.'[$key] = $value;';
                eval($php_stmt_att);
            }
        }
    }
    return $xml_array;
}
?>
