
package com.hellsingorg.topmodelphoto.DbHandler;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserFunctions {

	private static String api_base= "http://159.203.105.249/api/";
	private static String apiNewsHome = api_base + "ChalBhaiJaneDe";

	private OkHttpClient client = new OkHttpClient();

	public JSONObject ReadModelDetails(){

		System.out.println("Inside ReadVehicleDetails");
		String api_local = "http://192.168.43.225/laravel_project/public/api/";
		String apiNewsAll = api_local + "GetModelList";
		JSONObject json = null;
		RequestBody formBody = new FormBody.Builder()
				.build();
		Request request = new Request.Builder()
				.url(apiNewsAll)
				.post(formBody)
				.build();

		try {
			Response response = client.newCall(request).execute();
			json = new JSONObject( response.body().string());

			if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

			System.out.println(response.body().string());

		}
		catch (Exception e)
		{
			//	System.out.println(e);
			e.printStackTrace();
		}

			System.out.println(json);
		return json;
	}

	public JSONObject ReadCountryList(){

		String api_local = "http://192.168.43.225/laravel_project/public/api/";
		String apiNewsAll = api_local + "GetCountryList";
		JSONObject json = null;
		RequestBody formBody = new FormBody.Builder()
				.build();
		Request request = new Request.Builder()
				.url(apiNewsAll)
				.post(formBody)
				.build();

		try {
			Response response = client.newCall(request).execute();
			json = new JSONObject( response.body().string());

			if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

			System.out.println(response.body().string());

		}
		catch (Exception e)
		{
			//	System.out.println(e);
			e.printStackTrace();
		}

		System.out.println(json);
		return json;
	}

	public JSONObject ReadModelbyCountryId(String country_id){

		String api_local = "http://192.168.43.225/laravel_project/public/api/";
		String apiNewsAll = api_local + "GetModelByCountry";
		JSONObject json = null;
		RequestBody formBody = new FormBody.Builder()
				.add("country_id", country_id)
				.build();
		Request request = new Request.Builder()
				.url(apiNewsAll)
				.post(formBody)
				.build();

		try {
			Response response = client.newCall(request).execute();
			json = new JSONObject( response.body().string());

			if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

			System.out.println(response.body().string());

		}
		catch (Exception e)
		{
			//	System.out.println(e);
			e.printStackTrace();
		}

		System.out.println(json);
		return json;
	}

	public JSONObject ReadModelbyId(String model_id){

		String api_local = "http://192.168.43.225/laravel_project/public/api/";
		String apiNewsAll = api_local + "GetModelImage";
		JSONObject json = null;
		RequestBody formBody = new FormBody.Builder()
				.add("model_id", model_id)
				.build();
		Request request = new Request.Builder()
				.url(apiNewsAll)
				.post(formBody)
				.build();

		try {
			Response response = client.newCall(request).execute();
			json = new JSONObject( response.body().string());

			if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

			System.out.println(response.body().string());

		}
		catch (Exception e)
		{
			//	System.out.println(e);
			e.printStackTrace();
		}

		System.out.println(json);
		return json;
	}

	public JSONObject ReadModelStorybyId(String model_id){

		String api_local = "http://192.168.43.225/laravel_project/public/api/";
		String apiNewsAll = api_local + "GetModelStory";
		JSONObject json = null;
		RequestBody formBody = new FormBody.Builder()
				.add("model_id", model_id)
				.build();
		Request request = new Request.Builder()
				.url(apiNewsAll)
				.post(formBody)
				.build();

		try {
			Response response = client.newCall(request).execute();
			json = new JSONObject( response.body().string());

			if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

			System.out.println(response.body().string());

		}
		catch (Exception e)
		{
			//	System.out.println(e);
			e.printStackTrace();
		}

		System.out.println(json);
		return json;
	}

	public JSONObject ReadModelStoryAllList(){

		String api_local = "http://192.168.43.225/laravel_project/public/api/";
		String apiNewsAll = api_local + "GetModelAllStory";
		JSONObject json = null;
		RequestBody formBody = new FormBody.Builder()
				.build();
		Request request = new Request.Builder()
				.url(apiNewsAll)
				.post(formBody)
				.build();

		try {
			Response response = client.newCall(request).execute();
			json = new JSONObject( response.body().string());

			if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

			System.out.println(response.body().string());

		}
		catch (Exception e)
		{
			//	System.out.println(e);
			e.printStackTrace();
		}

		System.out.println(json);
		return json;
	}

}
