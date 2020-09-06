# Cracking_RSA-Java

Task: 

Alice and Bob use a secure channel to communicate. They encrypt messages
using the RSA cryptosystem. Their public keys are available in a public key
directory. Suppose you intercept a message that Alice sent to Bob. You assume
that Alice wants to be sure that only Bob can decrypt the message. You also
assume that Bob wants to be sure that only Alice could have sent the message.
You would like to decrypt the message and obtain the plaintext. You have been
provided with the ciphertext and Alice’s and Bob’s public keys.

Solution:
I have received a public-key (‘e’) and a modulus value (‘n’) for both Alice and Bob, alongside an encrypted ciphertext. 
message (m) = c^d mod n

d = 1/e mod r

The r value also known as Euler’s totient/phi, is calculated by:
r = (p − 1) ∗ (q − 1).

Therefore, we must calculate the values of ‘p’ and ‘q’ (where n = p ∗ q).
This requires us to factorize the modulus, to find which 2 prime factors multiplied equate to the value of ‘n’.

q = n / q

In order to factorize the modulus, I had opted for the Pollard Rho algorithm
