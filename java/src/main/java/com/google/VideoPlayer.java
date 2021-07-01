package com.google;

import java.lang.reflect.Array;
import java.util.*;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private Video currentlyBeingPlayed;
  private boolean isPaused;
  private ArrayList<VideoPlaylist> playlists;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    this.currentlyBeingPlayed = null;
    this.isPaused = false;
    this.playlists = new ArrayList<>();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }
  // sort videos and display them one by one - actual way of displaying is in toString method
  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");
    List<Video> sortedList = videoLibrary.getVideos();
    Collections.sort(sortedList);

    for(Video v: sortedList){
      StringBuilder video = new StringBuilder();
      video.append(v);
      if(v.isFlagged()) video.append(" - FLAGGED " + v.getReason());
      System.out.println(video);
    }

  }

  public void playVideo(String videoId) {

    Video nextVideo = videoLibrary.getVideo(videoId);

    if(currentlyBeingPlayed != null && nextVideo != null)
      System.out.println("Stopping video: " + currentlyBeingPlayed.getTitle());

    currentlyBeingPlayed = nextVideo;

    if(currentlyBeingPlayed == null)
      System.out.println("Cannot play video: Video does not exist");
    else if (currentlyBeingPlayed.isFlagged()){
      System.out.println("Cannot play video: Video is currently flagged " + currentlyBeingPlayed.getReason());
      currentlyBeingPlayed = null;
      isPaused = false;
    }
    else{
      System.out.println("Playing video: " + currentlyBeingPlayed.getTitle());
      isPaused = false;
    }

  }

  public void stopVideo() {
    if(currentlyBeingPlayed == null)
      System.out.println("Cannot stop video: No video is currently playing");
    else{
      System.out.println("Stopping video: " + currentlyBeingPlayed.getTitle());
      currentlyBeingPlayed = null;
      isPaused = false;
    }
  }

  public void playRandomVideo() {
    if(areAllVideosFlagged(videoLibrary.getVideos())){
      System.out.println("No videos available");
      return;
    }
    Random random = new Random();
    int videoIndex = random.nextInt(videoLibrary.getVideos().size());
    Video nextVideo = videoLibrary.getVideos().get(videoIndex);
    playVideo(nextVideo.getVideoId());
  }

  public void pauseVideo() {
    if(currentlyBeingPlayed == null) System.out.println("Cannot pause video: No video is currently playing");
    else if(isPaused) System.out.println("Video already paused: " + currentlyBeingPlayed.getTitle());
    else{
      isPaused = true;
      System.out.println("Pausing video: " + currentlyBeingPlayed.getTitle() );
    }
  }

  public void continueVideo() {
    if(currentlyBeingPlayed == null){
      System.out.println("Cannot continue video: No video is currently playing");
    }
    else if(isPaused){
      System.out.println("Continuing video: " + currentlyBeingPlayed.getTitle());
      isPaused = false;
    }
    else {  // always going to be not paused thus video is playing
      System.out.println("Cannot continue video: Video is not paused");
    }
  }

  public void showPlaying() {

    if(currentlyBeingPlayed == null){
      System.out.println("No video is currently playing");
      return;
    }

    StringBuilder outputMessage = new StringBuilder();
    outputMessage.append(currentlyBeingPlayed);

    if(isPaused)
      outputMessage.append(" - PAUSED");  // avoiding to write 2 print statements, thus adding - Paused to output.

    System.out.println("Currently playing: " + outputMessage);
  }

  public void createPlaylist(String playlistName) {

    if(getPlaylist(playlistName) != null) {
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
      return;
    }

    playlists.add(new VideoPlaylist(playlistName));

    System.out.println("Successfully created new playlist: " + playlistName);

  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    VideoPlaylist playlist = getPlaylist(playlistName);
    Video toAdd = videoLibrary.getVideo(videoId);
    if(playlist == null) {
      System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
    }
    else if(toAdd == null){
      System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
    }
    else if(toAdd.isFlagged()){
      System.out.println("Cannot add video to " + playlistName + ": Video is currently flagged " + toAdd.getReason());
    }
    else if(playlist.contains(toAdd)){
      System.out.println("Cannot add video to " + playlistName + ": Video already added");
    }
    else{
      playlist.add(toAdd);
      System.out.println("Added video to " + playlistName + ": " + toAdd.getTitle());
    }
  }

  public void showAllPlaylists() {
    if(playlists.isEmpty()) System.out.println("No playlists exist yet");
    else{
      Collections.sort(playlists);
      System.out.println("Showing all playlists:");
      for(VideoPlaylist playlist: playlists){
        System.out.println(playlist.getName());
      }
    }
  }

  public void showPlaylist(String playlistName) {
    VideoPlaylist playlist = getPlaylist(playlistName);
    if(playlist == null) System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
    else{
      System.out.println("Showing playlist: " + playlistName);
      ArrayList<Video> videos = playlist.getVideos();
      if(videos.isEmpty()) System.out.println("No videos here yet");
      else{
        for(Video v: videos) {
          StringBuilder video = new StringBuilder();
          video.append(v);
          if (v.isFlagged()) video.append(" - FLAGGED " + v.getReason());
          System.out.println(video);
        }
      }
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    VideoPlaylist playlist = getPlaylist(playlistName);
    Video video = videoLibrary.getVideo(videoId);
    if(playlist == null){
      System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
    }
    else if(video == null){
      System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
    }
    else if(!playlist.contains(video)){
      System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
    }
    else {
      System.out.println("Removed video from " + playlistName + ": " + video.getTitle());
      playlist.remove(video);
    }
  }

  public void clearPlaylist(String playlistName) {
    VideoPlaylist playlist = getPlaylist(playlistName);
    if(playlist == null) System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
    else{
      System.out.println("Successfully removed all videos from " + playlistName);
      playlist.clear();
    }
  }

  public void deletePlaylist(String playlistName) {
    VideoPlaylist playlist = getPlaylist(playlistName);
    if(playlist == null) System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
    else{
      System.out.println("Deleted playlist: " + playlistName);
      playlists.remove(playlist);
    }
  }

  public void searchVideos(String searchTerm) {
    ArrayList<Video> results = new ArrayList<>();
    for(Video video: videoLibrary.getVideos()){
      String title = video.getTitle().toLowerCase(Locale.ROOT);
      if(title.contains(searchTerm.toLowerCase(Locale.ROOT)) && !video.isFlagged()){
        results.add(video);
      }
    }
    searchEngine(results, searchTerm);
  }

  public void searchVideosWithTag(String videoTag) {
    ArrayList<Video> results = new ArrayList<>();
    for(Video video: videoLibrary.getVideos()){
       if(video.getTags().contains(videoTag.toLowerCase()) && !video.isFlagged()){
         results.add(video);
       }
    }
    searchEngine(results, videoTag);
  }

  public void flagVideo(String videoId) {
    flagVideo(videoId, "Not supplied");
  }

  public void flagVideo(String videoId, String reason) {
    Video video = videoLibrary.getVideo(videoId);
    if(video == null) System.out.println("Cannot flag video: Video does not exist");
    else if(video.isFlagged()) System.out.println("Cannot flag video: Video is already flagged");
    else{
      video.flag(reason);
      if(currentlyBeingPlayed != null){
        if(currentlyBeingPlayed.isFlagged()) stopVideo();
      }
      System.out.println("Successfully flagged video: " + video.getTitle() + " " + video.getReason());
    }
  }

  public void allowVideo(String videoId) {
    Video video = videoLibrary.getVideo(videoId);
    if(video == null){
      System.out.println("Cannot remove flag from video: Video does not exist");
    }
    else if(!video.isFlagged()){
      System.out.println("Cannot remove flag from video: Video is not flagged");
    }
    else{
      video.allow();
      System.out.println("Successfully removed flag from video: " + video.getTitle());
    }

  }

  private VideoPlaylist getPlaylist(String playlistName){
    for(VideoPlaylist playlist: playlists){
      if(playlist.getName().toLowerCase(Locale.ROOT).equals(playlistName.toLowerCase(Locale.ROOT))) {
        return playlist;
      }
    }
    return null;
  }

  private void searchEngine(ArrayList<Video> results, String searchTerm){
    if(results.isEmpty()) System.out.println("No search results for " + searchTerm);
    else{
      System.out.println("Here are the results for " + searchTerm + ":");
      Collections.sort(results);
      for(int i = 0; i < results.size(); i++){
        System.out.println(i+1 +") " + results.get(i));
      }
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");
      Scanner scanner = new Scanner(System.in);
      String input = scanner.nextLine();
      for(char c: input.toCharArray()){
        if(!Character.isDigit(c)) return;
      }
      int videoIndex = Integer.parseInt(input);
      if(videoIndex-1 < results.size() && videoIndex > 0) {
        playVideo(results.get(videoIndex - 1).getVideoId());
      }
    }
  }

  private boolean areAllVideosFlagged(List<Video> videos){
    for(Video video: videos){
      if (!video.isFlagged()) return false;
    }
    return true;
  }

}