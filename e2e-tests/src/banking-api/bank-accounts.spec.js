const chai = require("chai");
const should = chai.should();
const expect = chai.expect;
const app = require('../app')

describe("Bank accounts without auth headers", () => {
    it("should not be accessible", done => {
        app.bankingApi
            .get("/bank-accounts")
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(401);
                res.body.should.be.a("object");
                res.body.should.have.property("title");
                res.body.should.have.property("detail");
                done();
            });
    });
});

describe('Bank accounts for user 1', async () => {
    let authHeaders;
    before(async () => {
        authHeaders = await app.authHeaders('user1');
    });

    it("should get list for user1", done => {
        app.bankingApi
            .get("/bank-accounts")
            .set(authHeaders)
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(200);
                res.body.should.be.a("object");
                res.body.should.have.property("totalElements");
                expect(res.body.totalElements).to.equal(2);
                res.body.should.have.property("content");
                res.body.content.should.be.a("array");
                expect(res.body.content.length).to.equal(2);
                res.body.content[0].should.have.property("accountNumber");
                expect(res.body.content[0].accountNumber).to.equal("LU510011111111111111");
                res.body.content[1].should.have.property("accountNumber");
                expect(res.body.content[1].accountNumber).to.equal("LU090012222222222222");
                done();
            });
    });

});

describe('Bank accounts for user 2', async () => {
    let authHeaders;
    before(async () => {
        authHeaders = await app.authHeaders('user2');
    });

    it("should get list for user2", done => {
        app.bankingApi
            .get("/bank-accounts")
            .set(authHeaders)
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(200);
                res.body.should.be.a("object");
                res.body.should.have.property("totalElements");
                expect(res.body.totalElements).to.equal(2);
                res.body.should.have.property("content");
                res.body.content.should.be.a("array");
                expect(res.body.content.length).to.equal(2);
                res.body.content[0].should.have.property("accountNumber");
                expect(res.body.content[0].accountNumber).to.equal("LU640013333333333333");
                res.body.content[1].should.have.property("accountNumber");
                expect(res.body.content[1].accountNumber).to.equal("LU220014444444444444");
                done();
            });
    });

});
