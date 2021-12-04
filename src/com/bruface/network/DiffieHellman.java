package com.bruface.network;

import java.math.BigInteger;

public class DiffieHellman {
    BigInteger p,g;

    public DiffieHellman(BigInteger p, BigInteger g){
        this.p = p;
        this.g = g;
    }

    public BigInteger getG() {
        return g;
    }
    public BigInteger getP() {
        return p;
    }

    public BigInteger determineMessage(BigInteger secret){
        return this.g.modPow(secret,this.p);
    }

    public BigInteger determineKey(BigInteger message, BigInteger secret){
        return message.modPow(secret, this.p);
    }

}
