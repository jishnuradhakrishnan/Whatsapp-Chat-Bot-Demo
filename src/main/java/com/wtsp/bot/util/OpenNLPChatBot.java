package com.wtsp.bot.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import opennlp.tools.doccat.BagOfWordsFeatureGenerator;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.doccat.FeatureGenerator;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.model.ModelUtil;

/**
 * Custom chat bot or chat agent for automated chat replies for FAQs. It uses
 * different features of Apache OpenNLP for understanding what user is asking
 * for. NLP is natural language processing.
 * 
 * @author itsallbinary
 * Thank you itsallbinary
 *
 */
public class OpenNLPChatBot {
	public String generateReply(String msg) throws FileNotFoundException, IOException, InterruptedException {

		// Train categorizer model to the training data we created.
		DoccatModel model = trainCategorizerModel();

			// Break users chat input into sentences using sentence detection.
			String[] sentences = breakSentences(msg);

			String answer = "";
			boolean conversationComplete = false;

			// Loop through sentences.
			for (String sentence : sentences) {

				// Separate words from each sentence using tokenizer.
				String[] tokens = tokenizeSentence(sentence);

				// Tag separated words with POS tags to understand their gramatical structure.
				String[] posTags = detectPOSTags(tokens);

				// Lemmatize each word so that its easy to categorize.
				String[] lemmas = lemmatizeTokens(tokens, posTags);

				// Determine BEST category using lemmatized tokens used a mode that we trained
				// at start.
				String category = detectCategory(model, lemmas);

				System.out.println("category is >>>>"+category);
				// Get predefined answer from given category & add to answer.
				answer = answer + " " + ApplicationConstants.questionAnswer.get(category);

				// If category conversation-complete, we will end chat conversation.
				if ("conversation-complete".equals(category)) {
					conversationComplete = true;
				}
			}

			// Print answer back to user. If conversation is marked as complete, then end
			// loop & program.
			return answer;


	}

	/**
	 * Train categorizer model as per the category sample training data we created.
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static DoccatModel trainCategorizerModel() throws FileNotFoundException, IOException {
		// faq-categorizer.txt is a custom training data with categories as per our chat
		// requirements.
		InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(new File("faq-categorizer.txt"));
		ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
		ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

		DoccatFactory factory = new DoccatFactory(new FeatureGenerator[] { new BagOfWordsFeatureGenerator() });

		TrainingParameters params = ModelUtil.createDefaultTrainingParameters();
		params.put(TrainingParameters.CUTOFF_PARAM, 0);

		// Train a model with classifications from above file.
		DoccatModel model = DocumentCategorizerME.train("en", sampleStream, params, factory);
		return model;
	}

	/**
	 * Detect category using given token. Use categorizer feature of Apache OpenNLP.
	 * 
	 * @param model
	 * @param finalTokens
	 * @return
	 * @throws IOException
	 */
	private static String detectCategory(DoccatModel model, String[] finalTokens) throws IOException {
		String category = null;
		for (int i = 0; i < finalTokens.length; i++) {
			if (finalTokens[i].equals("angry")) {
				category = "angry-emoji";
			}
		}
		// Initialize document categorizer tool
		if (category == null) {
			DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);

			// Get best possible category.
			double[] probabilitiesOfOutcomes = myCategorizer.categorize(finalTokens);
			category = myCategorizer.getBestCategory(probabilitiesOfOutcomes);
			System.out.println("Category: " + category);
		}

		return category;

	}

	/**
	 * Break data into sentences using sentence detection feature of Apache OpenNLP.
	 * 
	 * @param data
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static String[] breakSentences(String data) throws FileNotFoundException, IOException {
		// Better to read file once at start of program & store model in instance
		// variable. but keeping here for simplicity in understanding.
		try (InputStream modelIn = new FileInputStream("en-sent.bin")) {

			SentenceDetectorME myCategorizer = new SentenceDetectorME(new SentenceModel(modelIn));

			String[] sentences = myCategorizer.sentDetect(data);

			for (int i = 0; i < sentences.length; i++) {
				String sentence = sentences[i];
				if (sentence.contains("😡") || sentence.contains("😠") || sentence.contains("🤬")) {
					sentences = new String[] { "angry" };
					break;
				}
			}
			return sentences;
		}
	}

	/**
	 * Break sentence into words & punctuation marks using tokenizer feature of
	 * Apache OpenNLP.
	 * 
	 * @param sentence
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static String[] tokenizeSentence(String sentence) throws FileNotFoundException, IOException {
		// Better to read file once at start of program & store model in instance
		// variable. but keeping here for simplicity in understanding.
		try (InputStream modelIn = new FileInputStream("en-token.bin")) {

			// Initialize tokenizer tool
			TokenizerME myCategorizer = new TokenizerME(new TokenizerModel(modelIn));

			// Tokenize sentence.
			String[] tokens = myCategorizer.tokenize(sentence);

			return tokens;

		}
	}

	/**
	 * Find part-of-speech or POS tags of all tokens using POS tagger feature of
	 * Apache OpenNLP.
	 * 
	 * @param tokens
	 * @return
	 * @throws IOException
	 */
	private static String[] detectPOSTags(String[] tokens) throws IOException {
		// Better to read file once at start of program & store model in instance
		// variable. but keeping here for simplicity in understanding.
		try (InputStream modelIn = new FileInputStream("en-pos-maxent.bin")) {

			// Initialize POS tagger tool
			POSTaggerME myCategorizer = new POSTaggerME(new POSModel(modelIn));

			// Tag sentence.
			String[] posTokens = myCategorizer.tag(tokens);

			return posTokens;

		}

	}

	/**
	 * Find lemma of tokens using lemmatizer feature of Apache OpenNLP.
	 * 
	 * @param tokens
	 * @param posTags
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	private static String[] lemmatizeTokens(String[] tokens, String[] posTags)
			throws InvalidFormatException, IOException {
		// Better to read file once at start of program & store model in instance
		// variable. but keeping here for simplicity in understanding.
		try (InputStream modelIn = new FileInputStream("en-lemmatizer.bin")) {

			// Tag sentence.
			LemmatizerME myCategorizer = new LemmatizerME(new LemmatizerModel(modelIn));
			String[] lemmaTokens = myCategorizer.lemmatize(tokens, posTags);

			return lemmaTokens;

		}
	}

}
