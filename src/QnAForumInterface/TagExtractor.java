/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QnAForumInterface;

/**
 * @author AJ
 */

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagExtractor {
    // Expanded set of common words and handling of contractions and possessives
    private static final Set<String> commonWords = new HashSet<>(Arrays.asList(
            "is", "are", "was", "were", "am", "the", "an", "a", "of", "and",
            "in", "on", "at", "by", "for", "with", "about", "as", "to", "from",
            "that", "which", "who", "whom", "whose", "this", "that", "these",
            "those", "there", "when", "where", "why", "how", "all", "any",
            "both", "each", "few", "more", "most", "other", "some", "such",
            "no", "nor", "not", "only", "own", "same", "so", "than", "too",
            "very", "do", "not", "you", "have", "had", "ve", "has", "be", "been",
            "can", "will", "just", "should", "now"
    ));

    public static String[] extractPotentialTags(String text) {
        // Normalize the text to lower case and handle contractions
        text = text.toLowerCase().replaceAll("\\b(can't)\\b", "cannot")
                .replaceAll("\\b(don't)\\b", "do not")
                .replaceAll("'s\\b", ""); // Handle possessive endings

        // Replace common words with empty space
        for (String word : commonWords) {
            text = text.replaceAll("\\b" + Pattern.quote(word) + "\\b", "");
        }

        // Extract remaining words as potential tags
        Set<String> tags = new HashSet<>();
        Matcher matcher = Pattern.compile("\\b[a-zA-Z]{2,}\\b").matcher(text); // Only words with two or more letters
        while (matcher.find()) {
            tags.add(matcher.group());
        }

        return tags.toArray(new String[0]);
    }
}

