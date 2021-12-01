const stdUtil = require('util');
const fs = require('fs');
const writeFilePromisified = stdUtil.promisify(fs.writeFile)
const appendFilePromisified = stdUtil.promisify(fs.appendFile)
const util = require('./util');

const errorFilename = 'failed_requests.txt'

const writeErrorToStdout = (error) => console.log(`ERROR: ${error}`);

module.exports = {
  writeErrorToStdout: writeErrorToStdout,

  readLines: (path) => {
    try {
      const content = fs.readFileSync(path).toString('utf-8');
      return content.split("\n").filter((url) => url !== '')
    } catch (e) {
      util.printAndExit('Could not read file: ' + e.message)
    }
  },

  writeErrorToFile: async(error) => {
    return await appendFilePromisified(errorFilename, error + "\n")
  },

  writeSuccessToFile: async(position, content) => {
    const fileName = `result_${position}.html`;

    return await writeFilePromisified(fileName, content)
  }
}
