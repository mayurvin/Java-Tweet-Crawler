
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import com.google.gson.Gson;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import twitter4j.HashtagEntity;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TweetEntity;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.auth.AccessToken;

public class GetUserStatus {

	public static void main(String[] args) {
		
		Query englishQuery = new Query("Paris Attack");
		englishQuery.setLang("en");
		Query germanQuery= new Query("Paris Angriff");
		germanQuery.setLang("de");
		Query russianQuery= new Query("Париж Атака");
		
		russianQuery.setLang("ru");
		Query arabicQuery= new Query("باريس هجوم");
		arabicQuery.setLang("ar");
		Query frenchQuery= new Query("Paris Attaque");
		frenchQuery.setLang("fr");
		
		List<Query> queryList=new ArrayList<Query>();
		//queryList.add(englishQuery);
		//queryList.add(russianQuery);
		//queryList.add(germanQuery);
		queryList.add(arabicQuery);
		//queryList.add(frenchQuery);

		Twitter twitter= new TwitterFactory().getInstance();
		twitter.setOAuthConsumer("2auhiiDGj3V8j0U7gotdjzM75","olL59ab0bGcekV8FeahdHI0308CXkVfRNThcpKeeSdsKWnm3X9");
		twitter.setOAuthAccessToken(new AccessToken("3523962137-u1cV7n6QmM0VXw16J6rHF2AgPt6y2Y7L6jtpUah","xfQqireAobRH7fL11Mf9WpgZDSZVtscDc9ZX9Zy0iCqWb"));

		try{
			QueryResult result;
			int count=0;
			for(Query query : queryList){	

				count++;	
				query.setCount(100);
				result=twitter.search(query);

				//result=twitter.search(englishQuery);

				//System.out.println("Twitter Counts for lang " + count + " : " + result.getCount());
				
				//------------
				/*int numberOfTweets = 2000;
			    long lastID = Long.MAX_VALUE;
			    ArrayList<Status> tweets = new ArrayList<Status>();
			    while (tweets.size () < numberOfTweets) 
			    {
			      if (numberOfTweets - tweets.size() > 100)
			        query.setCount(100);
			      else 
			        query.setCount(numberOfTweets - tweets.size());
			      //QueryResult result = twitter.search(query);
				tweets.addAll(result.getTweets());
				System.out.println("Gathered " + tweets.size() + " tweets"+"\n");
				for (Status t: tweets) 
				  if(t.getId() < lastID) 
				      lastID = t.getId();; 
			      query.setMaxId(lastID-1);
			    }*/
				//------------
				//List<Status> statusList = result.getTweets();   //Keval
				ArrayList<Status> tweets = new ArrayList<Status>();
				tweets.addAll(result.getTweets());
				List<UserEntity> userEntitiesList= new ArrayList<UserEntity>();
				for (Status tweet : tweets) {					

					UserEntity userEntity=new UserEntity();
					//userEntity.setText_en(tweet.getText());
					//userEntity.setText_ru(tweet.getText());
					//userEntity.setText_de(tweet.getText());
					userEntity.setText_ar(tweet.getText());
					//userEntity.setText_fr(tweet.getText());
					/*if(count==1){
						userEntity.setText_en(tweet.getText());
					}
					else if(count==2){
						userEntity.setText_ru(tweet.getText());
					}
					else if(count==3){
						userEntity.setText_de(tweet.getText());
					}
					else if(count==4){
						userEntity.setText_ar(tweet.getText());
					}
					else if(count==5){
						userEntity.setText_fr(tweet.getText());
					}*/
					
					userEntity.setCreated_at(GetUserStatus.tweeterToSolrDateConverter(tweet.getCreatedAt()));					
					userEntity.setTweetSource(tweet.getSource());
					userEntity.setUserId(tweet.getUser().getId());
					userEntity.setLang(tweet.getLang());
					userEntity.setUserLocation(tweet.getUser().getLocation());
					userEntity.setUserName(tweet.getUser().getName());
					userEntity.setUserScreenName(tweet.getUser().getScreenName());
					userEntity.setUserTimeZone(tweet.getUser().getTimeZone());
					userEntity.setMentionedURLInTweet(tweet.getUser().getURLEntity().getURL());
					userEntity.setUserDescription(tweet.getUser().getDescription());
					//userEntity.setTweet_urls(tweet.getUser().getURLEntity().getExpandedURL());
					
					List<String> urlEntitiesList= new ArrayList<String>();
					for(URLEntity urlEntity : tweet.getURLEntities()){
						urlEntitiesList.add(urlEntity.getExpandedURL());
					}
					userEntity.setTweet_urls(urlEntitiesList);
					
					List<String> hashtagsText = new ArrayList<String>();
					for(HashtagEntity hashtagEntity : tweet.getHashtagEntities()){
						hashtagsText.add(hashtagEntity.getText());
					}

					//System.out.println(hashtagsText);
					userEntity.setTweet_hashtags(hashtagsText);
					
					userEntitiesList.add(GetUserStatus.stanfordTagger(userEntity));
					
				}
				Gson gson= new Gson();
				String userEntityJSON = gson.toJson(userEntitiesList);
				//System.out.println("UserEntity : "+ userEntityJSON);
				new File("Json_Files_New").mkdir();
				//String fileName = "Json_Files/Arabic.json";
				FileOutputStream fileOutStream = null;
				OutputStreamWriter outStreamWriter = null;
				BufferedWriter bufferedWriter = null;
				try {
					fileOutStream = new FileOutputStream("Json_Files_New/Arabic_ParisAttack_1.json");
					outStreamWriter = new OutputStreamWriter(fileOutStream, "UTF-8");
					bufferedWriter = new BufferedWriter(outStreamWriter);
					bufferedWriter.write(userEntityJSON);
					bufferedWriter.flush();
					System.out.println("Successfully Done");
					/*if(count==1){
						fileOutStream = new FileOutputStream("Json_Files/English.json");
						outStreamWriter = new OutputStreamWriter(fileOutStream, "UTF-8");
						bufferedWriter = new BufferedWriter(outStreamWriter);
						bufferedWriter.write(userEntityJSON);
						bufferedWriter.flush();
						System.out.println("Successfully entered English data");
						/*
						FileWriter fileWriter=new FileWriter("JsonFiles/English.json", true);
						fileWriter.write(userEntityJSON);
						fileWriter.close();
						System.out.println("Successfully entered English data"); 
					}
					else if (count==2) {
						fileOutStream = new FileOutputStream("Json_Files/Russian.json");
						outStreamWriter = new OutputStreamWriter(fileOutStream, "UTF-8");
						bufferedWriter = new BufferedWriter(outStreamWriter);
						bufferedWriter.write(userEntityJSON);
						bufferedWriter.flush();
						/*FileWriter fileWriter=new FileWriter("JsonFiles/Russian.json", true);
						fileWriter.write(userEntityJSON);
						fileWriter.close();
						//System.out.println("UserEntity : "+ userEntityJSON);
						System.out.println("successfully entered Russian data");
					}
					else if (count==3){
						fileOutStream = new FileOutputStream("Json_Files/German.json");
						outStreamWriter = new OutputStreamWriter(fileOutStream, "UTF-8");
						bufferedWriter = new BufferedWriter(outStreamWriter);
						bufferedWriter.write(userEntityJSON);
						bufferedWriter.flush();
						/*
						FileWriter fileWriter=new FileWriter("JsonFiles/German.json", true);
						fileWriter.write(userEntityJSON);
						fileWriter.close();
						System.out.println("successfully entered German data");
					}
					else if (count==4){
						fileOutStream = new FileOutputStream("Json_Files/Arabic.json");
						outStreamWriter = new OutputStreamWriter(fileOutStream, "UTF-8");
						bufferedWriter = new BufferedWriter(outStreamWriter);
						bufferedWriter.write(userEntityJSON);
						bufferedWriter.flush();
						/*
						FileWriter fileWriter=new FileWriter("JsonFiles/Arabic.json", true);
						fileWriter.write(userEntityJSON);
						fileWriter.close();
						System.out.println("successfully entered Arabic data");
					}
					else if (count==5){
						fileOutStream = new FileOutputStream("Json_Files/French.json");
						outStreamWriter = new OutputStreamWriter(fileOutStream, "UTF-8");
						bufferedWriter = new BufferedWriter(outStreamWriter);
						bufferedWriter.write(userEntityJSON);
						bufferedWriter.flush();
						/*
						FileWriter fileWriter=new FileWriter("JsonFiles/French.json", true);
						fileWriter.write(userEntityJSON);
						fileWriter.close();
						System.out.println("successfully entered French data");
					}*/
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Error while creating File");
					e.printStackTrace();
				}
			}
			
		System.exit(0);
	}catch(TwitterException te) {
		te.printStackTrace();
		System.out.println("Failed to search tweets: " + te.getMessage());
		System.exit(-1);
	}
}
	
	public static String tweeterToSolrDateConverter(Date tweeterDate){
		DateFormat df = new SimpleDateFormat("YYYY-MM-dd'T'hh:mm:ss'Z'");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		return df.format(tweeterDate);
		
	}
	
	public static UserEntity stanfordTagger(UserEntity userEntity) {
		
		
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
	    Properties props = new Properties();
	    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, regexner");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    
	    // read some text in the text variable
	 /*   BufferedReader bufferReader = new BufferedReader(new FileReader(args[0]));
	    while ((wordString = bufferReader.readLine()) != null) {*/
	    //String text = userEntity.getText_en();
	    //String text = userEntity.getText_ru(); // Add your text here!
	    //String text = userEntity.getText_de();
	    String text = userEntity.getText_ar();
	    //String text = userEntity.getText_fr();
	    
	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);
	    
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    List<String> organizations=new ArrayList<String>();
	    List<String> person=new ArrayList<String>();
	    List<String> location=new ArrayList<String>();
	    for(CoreMap sentence: sentences) {
	      // traversing the words in the current sentence
	      // a CoreLabel is a CoreMap with additional token-specific methods
	    	
	      for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	    	 
	        // this is the text of the token
	        String word = token.get(TextAnnotation.class);
	        // this is the POS tag of the token
	        //String pos = token.get(PartOfSpeechAnnotation.class);
	        // this is the NER label of the token
	        String ne = token.get(NamedEntityTagAnnotation.class);  
	        if(ne.equals("ORGANIZATION")){
	        	if(!organizations.contains(word)){
	        	organizations.add(word);
	        	}
	        }
	        else if(ne.equals("LOCATION")){
	        	if(!location.contains(word)){
	        	location.add(word);
	        	}
	        }
	        else if(ne.equals("PERSON")){
	        	if(!person.contains(word)){
	        	person.add(word);
	        	}
	        }
	        // this is the sentiment label of the token
	        //String sentiment = token.get(SentimentCoreAnnotations.SentimentClass.class);
	        //this is the relationship label of the token MachineReadingAnnotations
	        //String relation = token.get(MachineReadingAnnotations.RelationMentionsAnnotation.class).toString();
	        
	        //System.out.println("word: "+word+", pos: "+pos+", ne: "+ne+", token:"+token.toString());
	      }

	      // this is the parse tree of the current sentence
	     // Tree tree = sentence.get(TreeAnnotation.class);
	      //System.out.println("tree: "+tree.toString());
	      // this is the Stanford dependency graph of the current sentence
	      //SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
	      //System.out.println("dependencies: "+dependencies.toString());
	    }
	    userEntity.setOrganizationTag(organizations);
	    userEntity.setLocationTag(location);
	    userEntity.setPersonTag(person);
	    // This is the coreference link graph
	    // Each chain stores a set of mentions that link to each other,
	    // along with a method for getting the most representative mention
	    // Both sentence and token offsets start at 1!
	   // Map<Integer, CorefChain> graph = 
	      document.get(CorefChainAnnotation.class);
	    System.out.println("userEntity:"+userEntity.toString());
	    return userEntity;
	}
	
}

