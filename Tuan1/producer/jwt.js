const jwt = require('jsonwebtoken');


const ACCESS_TOKEN_SECRET = process.env.ACCESS_TOKEN_SECRET || 'your_access_token_secret_key';
const REFRESH_TOKEN_SECRET = process.env.REFRESH_TOKEN_SECRET || 'your_refresh_token_secret_key';

function generateAccessToken(payload, expiresIn = '15m') {
  return jwt.sign(payload, ACCESS_TOKEN_SECRET, { expiresIn });
}

function generateRefreshToken(payload, expiresIn = '7d') {
  return jwt.sign(payload, REFRESH_TOKEN_SECRET, { expiresIn });
}

function generateTokens(payload) {
  const accessToken = generateAccessToken(payload);
  const refreshToken = generateRefreshToken(payload);
  
  return {
    accessToken,
    refreshToken,
    // expiresIn: '15m'
  };
}

function verifyAccessToken(token) {
  try {
    return jwt.verify(token, ACCESS_TOKEN_SECRET);
  } catch (error) {
    console.error('Invalid access token:', error.message);
    return null;
  }
}

function verifyRefreshToken(token) {
  try {
    return jwt.verify(token, REFRESH_TOKEN_SECRET);
  } catch (error) {
    console.error('Invalid refresh token:', error.message);
    return null;
  }
}

module.exports = {
  generateAccessToken,
  generateRefreshToken,
  generateTokens,
  verifyAccessToken,
  verifyRefreshToken
};
