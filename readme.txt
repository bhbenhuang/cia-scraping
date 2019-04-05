Ben Huang
NETS 150
ReadMe

Algorithm Explanations
General: I used the JSoup sample code to an arraylist of the countries listed on the low broadband version of the World FactBook. I also used the sample code to find the link to each of these pages inside each country. Then I used the get document from URL function to extract the contents of each document.

Q1: I set conditions to see if any given page on a country contains the continent name in a category by itself and if it contains the disaster in any other part of the page. Then, using the links of all of the countries from the general aspect, I examined each country’s page. I set conditions for these aspects, and if they were met for a specific country’s webpage, it added that country’s name through its URL index to the ArrayList in the return. 
Use: First enter continent, then enter disaster name.

Q2: Same start approach as (1), but instead I looked for the field “Flag description:” in any of the elements. Then, if there was one present, I found this element’s next sibling, which is the description of the flag. If this description contained the string of the feature I was looking for such as a star, then I returned this country.
Use: Enter Feature 

Q3: Same as above. First, I pulled a list of the countries in the continent in question. Then I found the area ranking of each of these countries using the same process as above. Then I found the country with the highest area ranking. 
Use: Enter the continent

Q4: I hardcoded Pennsylvania’s area into the program. Similar process in which I extract the area of each country, and if it’s less, then I add it to the return ArrayList.
Use: Enter the continent.

Q5: In question 5, I first find the organisation names of each of the organisations in appendix b. If these organisation content boxes contain the word “established”, then I return the name of the organisation. Then I find the year in each box through finding the first four consecutive integers to find the year founded. Then I place a mapping of the name to the year in a treemap. Then I compile an arraylist of the years, and sort it. Then, if I am given a parameter to find the earliest x number of companies, I return the index x value of this array list to find the “cut-off” year. Then I return all the values in the treemap that have a value lower than this value. This gets put into an ArrayList and returned. Note, sometimes more than the specified number of organisations is returned because I only look at the year.

Q6: I determine whether I am setting a lower or upper bound. Then using the percentage, extract the first religion from each religion box (because the World Factbook already lists each country’s dominant religion in order). If this percentage is lower or higher, depending on the question. Then I added the country to the list if the first religion that meets the requirement. I also extracted the country’s name and the religion’s name to the return array.
Use: First enter true if you want to set an upperbound (if you want to find the countries with a religion being a certain threshold). Then enter the bound. 
Note: I could not continually run this program. Therefore, each time running this question, please reset the console/compilation box. 

Q7: I determine whether each country has a coastline (if it doesn’t, it’s landlocked and meets the condition). If this is true, then I determine the number of neighbours it has. If it has only 1 other neighbour, then this is also true. If both these conditions are met, then the country is added to the arraylist of return.
Use: No inputs, as the question had no italicised aspects.

Q8: I determine the first character of each country’s internet address. If it matches the input character, I add it to the return ArrayList of countries. Similar process as above questions.
Use: enter the character you would like check if it is present. 

Assumptions: Since my program has specific aspect checks such as flag descriptions, it automatically gets rid of pages such as the Atlantic Ocean. Sometimes, unwanted parameters would be included such as “world” for question 6. However, I did not come up with an algorithm to filter out these responses. 

These questions were used as a sample. In the program the question number ask refers to this set of questions.
1. List countries in South America that are prone to earthquakes.
2. List all countries that have a star in their flag.
3. Find the country with the smallest population in Europe.
4. List the countries in Asia that have a smaller total area than Pennsylvania. (Note: You might need to look at some other resource to find Pennsylvania’s total 
5. List the 10 oldest International Organizations and Groups in chronological order of their dates of establishment. (No need to consider groups whose dates of establishment are excluded from their description.)
6. Certain countries have one dominant religion (in terms of fraction of the population) whereas other countries don’t. List countries (along with the religion) where the dominant religion accounts for more than 80% of the population. List countries (along with the religions) where the dominant religion accounts for less than 50% of the population.
7. A landlocked country is one that is entirely enclosed by land. For example, Austria is landlocked and shares its borders with Germany, Czech Republic, Hungary, etc. There are certain countries that are entirely landlocked by a single country. Find these countries.
8. Wild card – come up with an interesting question. List the question and find the answer to it.

Running Instructions:
- Import the .java file into an IDE
- Add the .jar file as an external JAR
- Run JSoupAttempt.java
