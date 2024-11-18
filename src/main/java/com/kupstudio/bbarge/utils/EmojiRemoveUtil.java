package com.kupstudio.bbarge.utils;

import com.kupstudio.bbarge.constant.common.CommonConstant;
import com.kupstudio.bbarge.exception.common.ConditionFailException;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;

public class EmojiRemoveUtil {

    public static String removeEmoji(String input) {
        if (StringUtils.isEmpty(input)) {
            return StringUtils.EMPTY;
        }

        String emojiRemoved = EmojiParser.removeAllEmojis(input);

        if (StringUtils.isBlank(emojiRemoved)) {
            throw new ConditionFailException(CommonConstant.OUTPUT_IS_BLANK);
        }

        return emojiRemoved;
    }

    public static String removeEmojiForFilter(String searchWord) {
        if (StringUtils.isBlank(searchWord)) {
            return null;
        }

        String emojiRemoved = EmojiParser.removeAllEmojis(searchWord);

        if (StringUtils.isBlank(emojiRemoved)) {
            return null;
        }

        return emojiRemoved;
    }
}
