package com.tutorialspoint;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/AudioService")
public class AudioService {

	AudioDao audioDao = new AudioDao();

	@GET
	@Path("/files")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Audio> getFiles(){
		return audioDao.getAllFiles();
	}
	
	@GET
	@Path("/file/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Audio getFileByID(@PathParam("id") int id){
		return audioDao.getFileByID(id);
	}

	@POST
	@Path("/fileUpload")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response fileUpload(@FormParam("id") int id,
			@FormParam("content") String content,
			@Context HttpServletResponse servletResponse) throws IOException{
		System.out.println("Id => "+id+"\nContent -> \n:"+content);
		Response response = new Response();
		response.setMessage("Success");
		return response; 
	}
	
	@POST
	@Path("/fileUploadRaw")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response fileUploadRaw(Audio audio) throws IOException{
		//System.out.println("Id => "+audio.getId()+"\nContent -> \n:"+audio.getContent());
		Response response;
		boolean result = audioDao.uploadFile(audio);
		if(result)
			response = new Response("Success");
		else response = new Response("Failure");
		return response; 
	}
	
	@POST
	@Path("/fileUploadBytes")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response fileUploadBytes(ByteAudio audio) throws IOException{
		//System.out.println("Id => "+audio.getId()+"\nContent -> \n:"+audio.getContent());
		Response response;
		boolean result = audioDao.uploadFileInBytes(audio);
		if(result)
			response = new Response("Success");
		else response = new Response("Failure");
		return response; 
	}
}
