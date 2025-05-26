export const REGEX = {
      EMAIL: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
      LOGIN_ID: /^[a-zA-Z0-9_]+$/, // Chỉ chứa a-z, A-Z, 0-9 và _
      STARTS_WITH_NUMBER: /^[0-9]/, // Bắt đầu bằng số
      HALF_WIDTH_KANA: /^[\uFF66-\uFF9F\uFF70]+$/, // Kana halfsize,
      HALF_WIDTH_ENGLISH: /^[\x00-\x7F]+$/, // Tiếng anh halfsize,
      HALF_WIDTH_NUMBER: /^[0-9]+$/, // Số halfsize,
};