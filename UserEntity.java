import java.util.Date;
import java.util.List;

public class UserEntity {


	private String text_en;
	private String  text_ru;
	private String text_de;
	private String text_ar;
	private String text_fr;
	private List<String> tweet_hashtags;
	private String created_at;
	private List<String> tweet_urls;
	private String lang;	
	private String mentionedURLInTweet;
	private String userName;
	private String userScreenName;
	private String userTimeZone;	
	private Long id;
	private String userLocation;
	private String tweetSource;
	private String userDescription;
	private List<String> organizationTag;
	private List<String> personTag;
	private List<String> locationTag;
	//private String userbackgroundImageURL;
	//private String userProfileImageURL;	



	public List<String> getTweet_hashtags() {
		return tweet_hashtags;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "UserEntity [text_en=" + text_en + ", text_ru=" + text_ru + ", text_de=" + text_de + ", text_ar=" + text_ar + ", text_fr=" + text_fr + ", tweet_hashtags="
				+ tweet_hashtags + ", created_at=" + created_at + ", tweet_urls=" + tweet_urls + ", lang=" + lang
				+ ", mentionedURLInTweet=" + mentionedURLInTweet + ", userName=" + userName + ", userScreenName="
				+ userScreenName + ", userTimeZone=" + userTimeZone + ", id=" + id + ", userLocation=" + userLocation
				+ ", tweetSource=" + tweetSource + ", userDescription=" + userDescription + ", organizationTag="
				+ organizationTag + ", personTag=" + personTag + ", locationTag=" + locationTag + "]";
	}

	public List<String> getOrganizationTag() {
		return organizationTag;
	}

	public void setOrganizationTag(List<String> organizationTag) {
		this.organizationTag = organizationTag;
	}

	public List<String> getPersonTag() {
		return personTag;
	}

	public void setPersonTag(List<String> personTag) {
		this.personTag = personTag;
	}

	public List<String> getLocationTag() {
		return locationTag;
	}

	public void setLocationTag(List<String> locationTag) {
		this.locationTag = locationTag;
	}

	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public void setTweet_hashtags(List<String> tweet_hashtags) {
		this.tweet_hashtags = tweet_hashtags;
	}

	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}


	public List<String> getTweet_urls() {
		return tweet_urls;
	}
	public void setTweet_urls(List<String> tweet_urls) {
		this.tweet_urls = tweet_urls;
	}
	public String getMentionedURLInTweet() {
		return mentionedURLInTweet;
	}
	public void setMentionedURLInTweet(String mentionedURLInTweet) {
		this.mentionedURLInTweet = mentionedURLInTweet;
	}


	public Long getUserId() {
		return id;
	}
	public void setUserId(Long userId) {
		this.id = userId;
	}

	public String getUserLocation() {
		return userLocation;
	}
	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserScreenName() {
		return userScreenName;
	}
	public void setUserScreenName(String userScreenName) {
		this.userScreenName = userScreenName;
	}
	public String getUserTimeZone() {
		return userTimeZone;
	}
	public void setUserTimeZone(String userTimeZone) {
		this.userTimeZone = userTimeZone;
	}

	public String getUserDescription() {
		return userDescription;
	}
	public void setUserDescription(String userDescription) {
		this.userDescription = userDescription;
	}

	public String getTweetSource() {
		return tweetSource;
	}
	public void setTweetSource(String tweetSource) {
		this.tweetSource = tweetSource;
	}
	public String getText_en() {
		return text_en;
	}
	public void setText_en(String text_en) {
		this.text_en = text_en;
	}
	public String getText_ru() {
		return text_ru;
	}
	public void setText_ru(String text_ru) {
		this.text_ru = text_ru;
	}
	public String getText_de() {
		return text_de;
	}
	public void setText_de(String text_de) {
		this.text_de = text_de;
	}
	public String getText_ar() {
		return text_ar;
	}
	public void setText_ar(String text_ar) {
		this.text_ar = text_ar;
	}
	public String getText_fr() {
		return text_fr;
	}
	public void setText_fr(String text_fr) {
		this.text_fr = text_fr;
	}
}
