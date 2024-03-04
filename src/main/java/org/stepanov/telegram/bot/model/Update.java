package org.stepanov.telegram.bot.model;

public record Update(
        Integer updateId,
        Message message,
        Message editedMessage,
        Message channelPost,
        Message editedChannelPost,
//    MessageReactionUpdated messageReaction,
//    MessageReactionCountUpdated messageReactionCount,
        InlineQuery inlineQuery,
//    ChosenInlineResult chosenInlineResult,
//    CallbackQuery callbackQuery,
//    ShippingQuery shippingQuery,
//    PreCheckoutQuery preCheckoutQuery,
//    Poll poll,
//    PollAnswer pollAnswer,
        ChatMemberUpdated myChatMember,
        ChatMemberUpdated chatMember
//    ChatJoinRequest chatJoinRequest,
//    ChatBoostUpdated chatBoost,
//    ChatBoostRemoved removedChatBoost
) {
}
