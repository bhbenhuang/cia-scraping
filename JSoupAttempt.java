import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.*;


	/**
	 * Created by andrewallace on 4/2/18. Modified by benhuang for questions.
	 */
	public class JSoupAttempt {
	    private String baseUrl;
	    private Document currentDoc;

	    /**
	     * Constructor that initializes the base URL and loads the document produced from that URL
	     */
	    public JSoupAttempt() {
	        this.baseUrl = 
	        		"https://www.cia.gov/library/publications/the-world-factbook/print/textversion.html";
	        try {
	            this.currentDoc = getDOMFromURL(baseUrl);
	        } catch (IOException e) {
	            System.out.println("Could not get the CIA WorldFactbook home page!");
	        }
	    }

	    public Document getCurrentDoc() {
	        return currentDoc;
	    }

	    /**
	     * Method to get the list of countries in the main block
	     * @return ArrayList<String>
	     */
	    public ArrayList<String> getElementsOnTapNav() {
	        ArrayList<String> ans = new ArrayList<>();
	        Element banner = currentDoc.getElementById("GetAppendix_TextVersion");
	        Elements words = banner.select("span").
	        		attr("style","GetAppendix_TextVersion"); 
	        for(Element word: words) { 
	            if(!word.text().isEmpty()) {
	                ans.add(word.text());
	            }
	        }
	        return ans;
	    }

	    /**
	     * Gets the base url for each country page
	     * @return baseUrl
	     */
	    public String getBaseUrl() {
	    		return baseUrl;
	    }
	    
	    /**
	     * Method to get the link of each country's page 
	     * @return ArrayList<String>
	     */
	    public ArrayList<String> getLinksForElementsOnNav() {
	        ArrayList<String> ans = new ArrayList<>();
	        Element block = currentDoc.getElementById("GetAppendix_TextVersion");
	        Elements links = block.select("a");
	        for (Element link : links) {
	        		String[] base = getBaseUrl().split("/");
	        		String url = "";
	        		for (int i = 0; i < 6; i++) {
	        			url = url + base[i] + "/";
	        		}
	        		char[] characterArrayOfEnding = (link.attr("href")).toCharArray();
	        		char[] getsRidOfBadFormatting = new char[characterArrayOfEnding.length - 3];
	        		for (int i = 3; i < characterArrayOfEnding.length; i++) {
	        			getsRidOfBadFormatting[i - 3] = characterArrayOfEnding[i];
	        		}
	        		url = url + String.valueOf(getsRidOfBadFormatting);
	        		ans.add(url);
	        }
	        return ans;
	    }

	    /**
	     * Method to get a Document from a String  URL
	     * @param u
	     * @return Document
	     * @throws IOException
	     */
	    public Document getDOMFromURL(String u) throws IOException {
	        URL url = new URL(u);
	        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	        StringBuilder sb = new StringBuilder();
	        String curr = in.readLine();
	        while(curr != null) {
	            sb.append(curr);
	            curr = in.readLine();
	        }
	        return Jsoup.parse(sb.toString());
	    }
	    
	    /**
	     * Question 1: List countries in (continent) that are prone to 
	     * (a disaster). This method returns the list of countries. 
	     * 
	     * @param continent a String of which continent we're looking for
	     * @param disaster the String of the natural disaster we're examining for proneness
	     * @return an ArrayList of the countries that are prone to the disaster in a given continent
	     */
	    public ArrayList<String> getQuestion1(String continent, String disaster) {

	    		ArrayList<String> prone = new ArrayList<String>();
	    		
	    		for (String url : getLinksForElementsOnNav()) {
		    		boolean continentCondition = false; 
		    		boolean disasterCondition = false;
	    			try {
						Document countryPage = getDOMFromURL(url);
						Element topLevel = countryPage.getElementById("content");
						Elements geography = topLevel.getElementsByClass("category_data");
						for (Element e : geography) {
							if ((e.text().equals(continent))) {
								continentCondition = true;
							} if (e.text().contains(disaster)) {
								disasterCondition = true;
							}
							
						}
						if (continentCondition && disasterCondition) {
							prone.add(getElementsOnTapNav().get(getLinksForElementsOnNav().indexOf(url)));
						} 
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("Qustion 1 Malfunction");
					}
	    		}
	    		return prone;
	    }
	    
	    /**
	     * Returns Question 2. Gets the countries with flags of the specified attribute. 
	     * 
	     * @param flagDesign the String of the attribute in the flag that needs to be found
	     * @return the ArrayList of strings of countries
	     */
	    public ArrayList<String> getQuestion2(String flagDesign) {
	    		ArrayList<String> flags = new ArrayList<String>();
	    		
	    		for (String url : getLinksForElementsOnNav()) {
	    			boolean flagCondition = false;
	    			
	    			try {
						Document countryPage = getDOMFromURL(url);
						Elements topLevel = countryPage.
								getElementsByAttributeValue("style", "padding-left:5px;"); 
						for (Element e1 : topLevel) {
							Elements insides = e1.select("a");
							for (Element e2 : insides) {
								if (e2.text().contains("Flag description")) {
									Element flagDescription = e1.nextElementSibling();
									if (flagDescription.text().contains(flagDesign)) {
										flagCondition = true;
										break;
									}
								}
							}
						}
						if (flagCondition) {
							flags.add(getElementsOnTapNav().get(getLinksForElementsOnNav().indexOf(url)));
						} 

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			
	    		}
	    		return flags;
	    }
	    
	    /**
	     * Returns Question 3
	     * @param continent of which is interested
	     * @return the String of the country with the smallest population on that continent
	     */
	    public String getQuestion3(String continent) {
	    		ArrayList<String> relevantCountries = new ArrayList<>();
	    		ArrayList<String> relevantCountryNames = new ArrayList<>();
	    		ArrayList<Integer> populations = new ArrayList<>();
	    		String country = "";
    			int maximumRank = 0;
	    	
	    		for (String url : getLinksForElementsOnNav()) {
	    			String thisCountry = getElementsOnTapNav().
	    					get(getLinksForElementsOnNav().indexOf(url));
	    			boolean continentCondition = false;
	    			try {
						Document countryPage = getDOMFromURL(url);
						Element topLevel = countryPage.getElementById("content");
						Elements geography = topLevel.getElementsByClass("category_data");
						for (Element e : geography) {
							if ((e.text().equals(continent))) {
								continentCondition = true;
							}
							
						}
						if (continentCondition) {
							relevantCountries.add(url);
							relevantCountryNames.add(thisCountry);
						} 
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("Qustion 3 Malfunction");
					}
	    		}
	    		
	    		for (String url : relevantCountries) {
	    			try {
						Document countryPage = getDOMFromURL(url);
						Elements topLevel = countryPage.
								getElementsByAttributeValue("style", "padding-left:5px;");
						for (Element e1 : topLevel) {
							if (e1.text().contains("Population:")) {
								Element populationRank = e1.nextElementSibling().nextElementSibling();
								Elements populationRankNumber = 
										populationRank.getElementsByClass("category_data");
								for (Element e2 : populationRankNumber) {
									Elements insides = e2.select("a");
									for (Element e3 : insides) {
										populations.add(Integer.parseInt(e3.text()));
									}
									
								}
							}
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
	    		
	    		for (Integer i : populations) {
	    			maximumRank = Math.max(maximumRank, i);
	    		}
	    		
	    		int indexNumber = populations.indexOf(maximumRank);
	    		country = relevantCountryNames.get(indexNumber + 1);
	    		
	    		return country;
	    }

	    /**
	     * Gets question 4, returns the list of countries with areas smaller than Pennsylvania
	     * @param continent String of continent
	     * @return the ArrayList of countries that satisfy this condition
	     */
	    public ArrayList<String> getQuestion4(String continent) {
    			ArrayList<String> relevantCountries = new ArrayList<>();
    			ArrayList<String> relevantCountryNames = new ArrayList<>();
    			ArrayList<String> areas = new ArrayList<>();
    		
    			// gets the links for all the countries in this continent
    			for (String url : getLinksForElementsOnNav()) {
    				String thisCountry = getElementsOnTapNav().
    						get(getLinksForElementsOnNav().indexOf(url));
    				boolean continentCondition = false;
    				try {
					Document countryPage = getDOMFromURL(url);
					Element topLevel = countryPage.getElementById("content");
					Elements geography = topLevel.getElementsByClass("category_data");
					for (Element e : geography) {
						if ((e.text().equals(continent))) {
							continentCondition = true;
						}
						
					}
					if (continentCondition) {
						relevantCountries.add(url);
						relevantCountryNames.add(thisCountry);
					} 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Qustion 3 Malfunction");
				}
    			}
    		
    			// gives area of each country
    			for (String url : relevantCountries) {
    				try {
					Document countryPage = getDOMFromURL(url);
					Elements topLevel = countryPage.
							getElementsByAttributeValue("style", "padding-left:5px;");
					for (Element e1 : topLevel) {
						if (e1.text().contains("Area:")) {
							Element areaUnbound = e1.nextElementSibling();
							Elements areaIntegers = areaUnbound.getElementsByClass("category_data");
							for (Element e2 : areaIntegers) {
								String[] areaWithoutNotation = e2.text().split(" ");
								String numericRepresentation = areaWithoutNotation[0];
								String[] justNumbers = numericRepresentation.split(",");
								String number = "";
								for (int i = 0; i < justNumbers.length; i++) {
									number = number + justNumbers[i];
								}
								if (Integer.parseInt(number) < 119280) {
									areas.add(relevantCountryNames.
											get(relevantCountries.indexOf(url)));
								}
							}
						}
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			}
    		
    			return areas;
    			}
	    
	    /**
	     * Returns questions 6, a list of all the 
	     * countries with more than a certain percentage of the population as one religion
	     * @param percentage integer of the population
	     * @return ArrayList of all the countries
	     */
	    public ArrayList<String> getQuestion6(boolean upperbound, int percentage) {
			ArrayList<String> religiousCountries = new ArrayList<>();
		
			// gives area of each country
			for (String url : getLinksForElementsOnNav()) {
				try {
					boolean religion = true;
					Document countryPage = getDOMFromURL(url);
					Elements topLevel = countryPage.
							getElementsByAttributeValue("style", "padding-left:5px;");
					for (Element e1 : topLevel) {
						if (e1.text().contains("Religions:")) {
							Element religionDescriptionBox = e1.nextElementSibling();
							Elements religionDescription = 
									religionDescriptionBox.getElementsByClass("category_data");
							for (Element e2 : religionDescription) {
								String[] descriptionBreakdown = e2.text().split(" ");
								String countryName = 
										getElementsOnTapNav().
										get(getLinksForElementsOnNav().indexOf(url));
								String religionName = "";
								for (int i = 0; i < descriptionBreakdown.length; i++) {
									religionName = religionName + descriptionBreakdown[i] + " ";
									if (descriptionBreakdown[i].charAt(0) >= '0'
											&& descriptionBreakdown[i].charAt(0) <= '9') {
										break;
									}
								}
								String digits = "";
								for (int i = 0; i < e2.text().length() - 1; i++) {
									if ((e2.text().charAt(i) >= '0') && (e2.text().charAt(i) <= '9') 
											&& (e2.text().charAt(i+1) >= '0') && 
											(e2.text().charAt(i+1) <= '9')) {
										religion = true;
										digits = digits + e2.text().charAt(i)
												+ e2.text().charAt(i + 1);
										break;
									}
									religion = false;
								}

								if (religion) {
									if (upperbound) {
										if (Integer.parseInt(digits) <= percentage) {
											religiousCountries.add(countryName + " - " 
										+ religionName);
											}
										} else {
											if (Integer.parseInt(digits) >= percentage) {
												religiousCountries.add(countryName + " - "
											+ religionName);
												}
										}
									
									
								}
							}
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return religiousCountries;
			}
	   
	    /**
	     * returns question 7, the countries that are land-locked and only have one border
	     * @return the ArrayList of all the countries that has this condition
	     */
	    public ArrayList<String> getQuestion7() {
			ArrayList<String> relevantCountries = new ArrayList<>();
		
			// checks whether there is a coastline
			for (String url : getLinksForElementsOnNav()) {
				try {
					boolean coastlineCondition = false;
					boolean borderCondition = false;
					Document countryPage = getDOMFromURL(url);
					Elements topLevel = countryPage.
							getElementsByAttributeValue("style", "padding-left:5px;");
					for (Element e1 : topLevel) {
						if (e1.text().contains("Coastline:")) {
							Element coastline = e1.nextElementSibling();
							String[] information = coastline.text().split(" ");
							char coast = (information[0]).charAt(0);
							if (coast == '0') {
								coastlineCondition = true;
								
								// checks whether there is only one land boundary
								for (Element e2 : topLevel) {
									if (e2.text().contains("Land boundaries:")) {
										Element landBoundary = e2.nextElementSibling().nextElementSibling();
										Elements landBoundarySpecifics = 
												landBoundary.getElementsByClass("category");
										for (Element e3 : landBoundarySpecifics) {
											if (e3.text().contains("1")) {
												borderCondition = true;
											}
										}
									}
								}
							}
						}
					}
					if (coastlineCondition && borderCondition) {
						relevantCountries.add(getElementsOnTapNav().
								get(getLinksForElementsOnNav().indexOf(url)));
					}
				}
				catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			}
			return relevantCountries;
			}
	    /**
	     * Question 8: Because we're in a NETS class and have to relate 
	     * everything back to the internet, given a letter l (can be any letter), 
	     * which countries have an country intenet code that start with the given letter?
	     * @param startCharacter the first character of the internet domain name
	     * @return the ArrayList of countries that have the start letter in the internet domain
	     */
	    public ArrayList<String> getQuestion8(char startCharacter) {
	    		ArrayList<String> relevantCountries = new ArrayList<>();
	    		
	    		for (String url : getLinksForElementsOnNav()) {
	    			Document countryPage;
					try {
						countryPage = getDOMFromURL(url);
						Elements topLevel = countryPage.
								getElementsByAttributeValue("style", "padding-left:5px;");
						for (Element e1 : topLevel) {
							if (e1.text().contains("Internet country code:")) {
								Element countryCode = e1.nextElementSibling();
								String address = countryCode.text();
								if (address.charAt(1) == startCharacter) {
									relevantCountries.
									add(getElementsOnTapNav().get(getLinksForElementsOnNav().
											indexOf(url)));
								}
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
	    	
			return relevantCountries;
	    }
	    
	    public boolean isDigit(char character) {
	    		boolean condition = false;
	    	
	    		if (character >= '0' && character <= '9') {
	    			condition = true;
	    		}
	    	
	    	return condition;
	    }
	    
	    /**
	     * Returns question 5
	     * @param parameter the number of organisations to return
	     * @return the ArrayList of Integers
	     */
	    public ArrayList<String> getQuestion5(int parameter) {
	    		Map<String, String> relevantOrganisations = new TreeMap<String, String>();
	    		String appendixUrl = 
	    				"https://www.cia.gov/library/publications/the-world-factbook/appendix/appendix-b.html";
	    		ArrayList<String> relevantNames = new ArrayList<String>();
	    		ArrayList<Integer> organisationDates = new ArrayList<Integer>();
	    		
	    		try {
					Document organisations = getDOMFromURL(appendixUrl);
					Elements orgNames = organisations.
							getElementsByAttributeValue("style", "padding: 3px; display: block;");
					for (Element e1 : orgNames) {
						Element orgContent = e1.nextElementSibling();
						if (orgContent.text().contains("established")) {
							String content = orgContent.text();
							String year = "";
							for (int i = 0; i < content.length() - 4; i++) {
								if (isDigit(content.charAt(i)) && 
										isDigit(content.charAt(i + 1)) && isDigit(content.charAt(i + 2)) 
										&& isDigit(content.charAt(i+3))) {
									year = year + content.charAt(i) + content.charAt(i + 1) + 
											content.charAt(i + 2) + content.charAt(i + 3);
									relevantOrganisations.put(e1.text(), year);
									organisationDates.add(Integer.parseInt(year));
									break;
								}
							}
						}
					}
					
					Collections.sort(organisationDates);
					int limit = organisationDates.get(parameter);
					
					for (Map.Entry<String, String> entry : relevantOrganisations.entrySet()) {
						if (Integer.parseInt(entry.getValue()) < limit) {
							relevantNames.add(entry.getKey());
						}
						
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    		
	    		return relevantNames;
	    }
	    

	    public static void main(String[] args) {
	        JSoupAttempt jse = new JSoupAttempt();
	        Scanner sc=new Scanner(System.in); 
			while (true) {
				System.out.println("Enter your question number:");
				int Q = sc.nextInt();
				switch(Q) {
				case 1:
					Scanner sc1 =new Scanner(System.in);
					System.out.println("Enter your continent:");
					String continent1 = sc1.nextLine();
					System.out.println("Enter your disaster:");
					String disaster = sc1.nextLine();
					System.out.println(jse.getQuestion1(continent1, disaster));
					break;
				case 2: 
					Scanner sc2 =new Scanner(System.in);
					System.out.println("Enter your flag feature:");
					String feature = sc2.nextLine();
					System.out.println(jse.getQuestion2(feature));
					break;
				case 3:
					Scanner sc3 =new Scanner(System.in);
					System.out.println("Enter your continent:");
					String continent3 = sc3.nextLine();
					System.out.println(jse.getQuestion3(continent3));
					break;
				case 4:
					Scanner sc4 =new Scanner(System.in);
					System.out.println("Enter your continent:");
					String continent4 = sc4.nextLine(); 
					System.out.println(jse.getQuestion4(continent4));
					break;
				case 5: 
					Scanner sc5 =new Scanner(System.in);
					System.out.println("Enter the number of companies you desire:");
					int parameter = sc5.nextInt();
					System.out.println(jse.getQuestion5(parameter));
				case 6:
					Scanner sc6 =new Scanner(System.in);
					System.out.println("Enter whether you want to set an upperbound:");
					boolean upperbound = sc6.nextBoolean();
					System.out.println("Enter your percentage bound:");
					int percentage = sc6.nextInt();
					System.out.println(jse.getQuestion6(upperbound, percentage));
					break;
				case 7:
					System.out.println("Now printing all landlocked, single-bordered countries...");
					System.out.println(jse.getQuestion7());
					break;
				case 8:
					Scanner sc8 =new Scanner(System.in);
					System.out.println("Enter the first letter of the "
							+ "internet address you would like to find:");
					String character = sc8.nextLine();
					char input = character.charAt(0);
					System.out.println(jse.getQuestion8(input));
					break;
				default: 
					System.out.println("invalid question");
					break;
				   }
			   }
	    }
	}