package com.major.pgmanagement.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudinaryImageService {

	private static final String PG_IMAGE_FOLDER = "staynext/pg-images";

	@Value("${cloudinary.cloud-name:}")
	private String cloudName;

	@Value("${cloudinary.api-key:}")
	private String apiKey;

	@Value("${cloudinary.api-secret:}")
	private String apiSecret;

	public String uploadPgImage(MultipartFile image) {
		if (image == null || image.isEmpty()) {
			return null;
		}
		validateConfiguration();

		try {
			long timestamp = Instant.now().getEpochSecond();
			String signature = sha1("folder=" + PG_IMAGE_FOLDER + "&timestamp=" + timestamp + apiSecret);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", new MultipartFileResource(image));
			body.add("api_key", apiKey);
			body.add("timestamp", String.valueOf(timestamp));
			body.add("folder", PG_IMAGE_FOLDER);
			body.add("signature", signature);

			Map<?, ?> response = RestClient.create()
					.post()
					.uri("https://api.cloudinary.com/v1_1/{cloudName}/image/upload", cloudName)
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.body(body)
					.retrieve()
					.body(Map.class);

			if (response == null || response.get("secure_url") == null) {
				throw new RuntimeException("Cloudinary upload failed. No secure image URL returned.");
			}
			return response.get("secure_url").toString();
		} catch (Exception ex) {
			throw new RuntimeException("Unable to upload PG image to Cloudinary. Please try again.", ex);
		}
	}

	private void validateConfiguration() {
		if (!StringUtils.hasText(cloudName) || !StringUtils.hasText(apiKey) || !StringUtils.hasText(apiSecret)) {
			throw new RuntimeException("Cloudinary credentials are missing. Please configure CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY, and CLOUDINARY_API_SECRET.");
		}
	}

	private String sha1(String value) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
			return HexFormat.of().formatHex(hash);
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException("SHA-1 algorithm is not available.", ex);
		}
	}

	private static class MultipartFileResource extends ByteArrayResource {

		private final String filename;

		MultipartFileResource(MultipartFile multipartFile) throws java.io.IOException {
			super(multipartFile.getBytes());
			this.filename = StringUtils.hasText(multipartFile.getOriginalFilename())
					? multipartFile.getOriginalFilename()
					: "pg-image";
		}

		@Override
		public String getFilename() {
			return filename;
		}
	}
}
