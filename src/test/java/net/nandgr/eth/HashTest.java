package net.nandgr.eth;

import net.nandgr.eth.ethereumjcrypto.HashUtil;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;

public class HashTest {

    @Test
    public void test_crypto() throws Exception {
        String input = "";
        String expected = "c5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470";
        test(input, expected);

        input = "e2689cd4a84e23ad2f564004f1c9013e9589d260bde6380aba3ca7e09e4df40c";
        expected = "aee730442f53e7c9c7520f564428a7c7e5e34684dc00fd41095386c86ca6078a";
        test(input, expected);

        input = "00000000000000000000000000000000000000000000000000000000000000050000000000000000000000000000000000000000000000000000000000000001";
        expected = "e2689cd4a84e23ad2f564004f1c9013e9589d260bde6380aba3ca7e09e4df40c";
        test(input, expected);

        input = "5c5faf66f32e0f8311c32e8da8284a4ed60891a5a7e50fb2956b3cbaa79fc66ca376460e100415401fc2b8518c64502f187ea14bfc9503759705";
        expected = "549db056b65edf7d05bd66661b6d0a39b29b825bc80910f8bf7060a53bff68e1";
        test(input, expected);

        input = "3a3a819c48efde2ad914fbf00e18ab6bc4f14513ab27d0c178a188b61431e7f5623cb66b23346775d386b50e982c493adbbfc54b9a3cd383382336a1a0b2150a15358f336d03ae18f666c7573d55c4fd181c29e6ccfde63ea35f0adf5885cfc0a3d84a2b2e4dd24496db789e663170cef74798aa1bbcd4574ea0bba40489d764b2f83aadc66b148b4a0cd95246c127d5871c4f11418690a5ddf01246a0c80a43c70088b6183639dcfda4125bd113a8f49ee23ed306faac576c3fb0c1e256671d817fc2534a52f5b439f72e424de376f4c565cca82307dd9ef76da5b7c4eb7e085172e328807c02d011ffbf33785378d79dc266f6a5be6bb0e4a92eceebaeb1";
        expected = "348fb774adc970a16b1105669442625e6adaa8257a89effdb5a802f161b862ea";
        test(input, expected);
    }

    private static void test(String input, String expected) throws DecoderException {
        byte[] bytes = HashUtil.sha3(Hex.decodeHex(input));
        String result = Hex.encodeHexString(bytes);
        Assert.assertTrue(Arrays.equals(expected.getBytes(), result.getBytes()));
    }
}
