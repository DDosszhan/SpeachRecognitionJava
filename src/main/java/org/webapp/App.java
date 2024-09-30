package org.webapp;

import com.assemblyai.api.AssemblyAI;
import com.assemblyai.api.resources.transcripts.types.*;

public final class App {

    public static void main(String... args) throws Exception {
        AssemblyAI client = AssemblyAI.builder()
                .apiKey("API_KEY_GOES_HERE")
                .build();

        String url = "https://drive.google.com/uc?export=download&id=1CEfzmfo1Aaz5f7OjNy54Vb-1n_AEfe26";

        var config = TranscriptOptionalParams.builder()
                .speakerLabels(true)
                .build();

        Transcript transcript = client.transcripts().transcribe(url, config);

        if (transcript.getStatus() == TranscriptStatus.ERROR) {
            throw new Exception("Transcript failed with error: " + transcript.getError().get());
        }

        System.out.println("Transcript:\n");
        String formattedTranscript = formatTranscript(String.valueOf(transcript.getText()));
        System.out.println(formattedTranscript);

        transcript.getUtterances().ifPresent(utterances ->
                utterances.forEach(utterance ->
                        System.out.println("Speaker " + utterance.getSpeaker() + ": " + utterance.getText() + "\n")));
    }

    private static String formatTranscript(String text) {
        String[] sentences = text.split("(?<=[.!?])\\s*");

        return String.join("\n\n", sentences);
    }
}
