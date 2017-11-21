/*
 * Copyright (c) [2016] [ <ether.camp> ]
 * This file is part of the ethereumJ library.
 *
 * The ethereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ethereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the ethereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package net.nandgr.eth.ethereumjcrypto;

import net.nandgr.eth.ethereumjcrypto.jce.SpongyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;

public class HashUtil {

    private static final Logger log = LoggerFactory.getLogger(HashUtil.class);
    private static final Provider CRYPTO_PROVIDER;
    private static final String HASH_256_ALGORITHM_NAME;
    private static final MessageDigest sha256digest;

    static {
        Security.addProvider(SpongyCastleProvider.getInstance());
        CRYPTO_PROVIDER = Security.getProvider("SC");
        HASH_256_ALGORITHM_NAME = "ETH-KECCAK-256";
        try {
            sha256digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            log.error("Can't initialize HashUtils", e);
            throw new RuntimeException(e); // Can't happen.
        }
    }

    /**
     * @param input
     *            - data for hashing
     * @return - sha256 hash of the data
     */
    public static byte[] sha256(byte[] input) {
        return sha256digest.digest(input);
    }

    public static byte[] sha3(byte[] input) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(HASH_256_ALGORITHM_NAME, CRYPTO_PROVIDER);
            digest.update(input);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            log.error("Can't find such algorithm", e);
            throw new RuntimeException(e);
        }

    }

    /**
     * hashing chunk of the data
     * 
     * @param input
     *            - data for hash
     * @param start
     *            - start of hashing chunk
     * @param length
     *            - length of hashing chunk
     * @return - keccak hash of the chunk
     */
    public static byte[] sha3(byte[] input, int start, int length) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(HASH_256_ALGORITHM_NAME, CRYPTO_PROVIDER);
            digest.update(input, start, length);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            log.error("Can't find such algorithm", e);
            throw new RuntimeException(e);
        }
    }
}
