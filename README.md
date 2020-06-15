# AI---Web-Opinion-Visualiser

AI Project  

Heurisitic Search
Most of the code should be commented with what it does.  

For the scoring i have 3 values set from the original tutorial video, titles, headings and paragraphs. I wanted to add parameters to the scoring such as highlighted text, making header tags from 1-5 have different values and adding meta tags which you can see in Enum WordScocer class, but i didn't have time to implement and refactor parts of the code.  

To prevent high memory and to speed up the app the max links visted set to 60-100.  

The Heurisitic Search goes as follows  
1-----| Start with an URL + Search term connect to it, start to score it, add it to the Closed list, throw it onto the PriorityQueue and start Process() 

2-----| While the queue is empty and the closed list is not over the max pages limit, get the next element from the queue, get its document, get its edges  

3-----|	and for each edge get its link, if the link is not null and closed list is not over max pages limit, connect to the link connected to the webpage(child)   

4-----|	and score it and throw it onto the queue and return frequency.  

DuckDuckGoSearch is really simple you have your base URL https://duckduckgo.com/html/?q=
You then connect to that base URL, add a query to it, so a query of "Galway" would look like https://duckduckgo.com/html/?q=Galway  

Fuzzy Logic:
The fuzzy logic is the base from our labs and just reformatted to fit a Title, Heading and Body scoring
The Terms are significant, insignificant, relevant, irrelevant, low, average, high

There are 12 Inference Rules to cover all terms, score being low, avg or high

--ref--

https://jsoup.org/cookbook/  
https://jsoup.org/apidocs/  
http://jfuzzylogic.sourceforge.net/html/manual.html  
resources on moodle, videos + fuzzylogic fcl  
https://medium.com/@sethsubr/fetch-duckduckgo-web-search-results-in-20-lines-of-java-code-3a34ea9da085  
https://docs.oracle.com/javase/8/docs/api/index.html?java/util/concurrent/Callable.html  
