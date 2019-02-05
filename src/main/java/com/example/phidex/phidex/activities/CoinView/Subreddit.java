package com.example.phidex.phidex.activities.CoinView;

import java.util.HashMap;

public final class Subreddit {
    private static final HashMap<String, String> data;

    static {
        data = new HashMap<>();

        data.put("bitcoin", "bitcoin%2Bbtc");
        data.put("ethereum", "ethereum%2Bethtrader");
        data.put("bitcoin-cash", "bitcoincash");
        data.put("litecoin", "litecoin%2Blitecoinmarkets");
        data.put("cardano", "cardano%2Bcardanocoin");
        data.put("iota", "iota%2Biotamarkets");
        data.put("tron", "tronix");
        data.put("neo", "neo%2Bneotrader");
        data.put("dash", "dashpay");
        data.put("vechain", "vechain%2Bvechaintrader");
        data.put("bytecoin-bcn", "bytecoinbcn");
        data.put("ethereum-classic", "EthereumClassic");
        data.put("icon", "helloicon");
        data.put("binance-coin", "binance");
        data.put("omisego", "omise_go");
        data.put("zcash", "zec");
        data.put("bitcoin-gold", "bitcoingoldhq");
        data.put("ontology", "ontologynetwork");
        data.put("0x", "0xproject");
        data.put("verge", "verge%2Bvergecurrency");
        data.put("nano", "nanocurrency%2Bnanotrade");
        data.put("siacoin", "siacoin%2Bsiatrader");
        data.put("bytom", "bytomblockchain");
        data.put("waves", "waves%2Bwavesplatform");
        data.put("bitcoin-diamond", "bitcoindiamond");
        data.put("maker", "makerdao");
        data.put("populous", "populous_platform");
        data.put("golem-network-tokens", "gnttrader");
        data.put("bitcoin-private", "bitcoinprivate%2Bbitcoin_private");
        data.put("status", "statusim");
        data.put("aion", "aion%2Baionnetwork");
        data.put("hshare", "hshare%2Bhcash");
        data.put("waltonchain", "waltonchain");
        data.put("ark", "arkecosystem");
        data.put("digixdao", "digixdao%2Bdigix");
        data.put("basic-attention-token", "basicattentiontoken%2Bbatproject");
        data.put("loopring", "loopring%2Bloopringorg");
        data.put("nebulas-token", "nebulas");
        data.put("aelf", "aelf%2Baelfofficial");
        data.put("komodo", "komodoplatform");
        data.put("loom-network", "loomnetwork");
        data.put("kyber-network", "kybernetwork");
        data.put("kucoin-shares", "kucoin");
        data.put("kin", "kinfoundation%2Bkin");
        data.put("polymath-network", "polymathnetwork");
        data.put("ethos", "ethos_io");
        data.put("cryptonex", "cryptonex-cnx");
        data.put("fusion", "fusionfoundation");
        data.put("cortex", "cortex_official");
        data.put("gxchain", "gxs");
        data.put("qash", "qash%2Bliquidqash");
        data.put("funfair", "funfairtech");
        data.put("dragonchain", "dragonchain%2Bdrgn");
        data.put("enigma-project", "enigmaproject%2Benigmacatalyst");
        data.put("cybermiles", "cybermiles");
        data.put("matrix-ai-network", "matrixainetwork");
        data.put("huobi-token", "huobi");
        data.put("storm", "stormtoken");
        data.put("salt", "saltcoin%2Bsalttrader");
        data.put("nuls", "nulsservice");
        data.put("nxt", "next");
        data.put("power-ledger", "powerledger");
        data.put("maidsafecoin", "maidsafecoin");
        data.put("chainlink", "chainlink%2Blinktrader");
        data.put("monaco", "monacocard");
        data.put("wax", "wax_io");
        data.put("electroneum", "electroneum");
        data.put("revain", "revain_org%2Brvntrader");
        data.put("nucleus-vision", "nucleusvision");
        data.put("achain", "achain_official");
        data.put("request-network", "requestnetwork");
        data.put("civic", "civicplatform");
        data.put("deepbrain-chain", "deepbrainchain");
        data.put("poa-network", "poa");
        data.put("theta-token", "thetatoken");
        data.put("scryinfo", "scryddd");
        data.put("bitcoindark", "bitcoindark");
        data.put("sophiatx", "sophiatxproject");
        data.put("aragon", "aragonproject");
        data.put("zencash", "zensys");
        data.put("nexus", "nexusearth");
        data.put("time-new-bank", "timenewbank");
        data.put("credits", "creditscrypto");
        data.put("bluzelle", "bluezelle");
        data.put("ruff", "ruffchain");
        data.put("game", "gamecreditscrypto");
        data.put("gnosis-gno", "gnosispm");
    }

    public static String getSubreddit(String coinId) {
        String value = data.get(coinId);
        if (value != null) {
            return value;
        } else {
            return coinId;
        }
    }
}