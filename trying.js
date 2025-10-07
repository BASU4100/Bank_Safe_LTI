const fs = require('fs');
const { exec, execSync } = require('child_process');
const { promisify } = require('util');
const path = require('path');
const execAsync = promisify(exec);


const _is_progressive_or_capstone = process.argv[2] === 'true';

console.log('value of _is_progressive_or_capstone', _is_progressive_or_capstone);

const addModifiedCodeSolutionJs = () => {
    // File paths
    const jsFilePath = path.resolve('./solution.js');
    const textFilePath = path.resolve('./check-testcases.txt');
    try {
        // Read the string to append
        const stringToAppend = fs.readFileSync(textFilePath, 'utf8').trim();

        // Read the JavaScript file
        const jsFileContent = fs.readFileSync(jsFilePath, 'utf8');

        // Define regex pattern to find main() function calls (with optional spaces)
        const mainFunctionRegex = /main\(\s*\)\s*;?\s*/g;

        // Replace all instances of main() with the new content before it
        const updatedJsFileContent = jsFileContent.replace(
            mainFunctionRegex,
            `${stringToAppend}\nmain();\n`
        );

        // Write the updated content back to the JavaScript file
        fs.writeFileSync(jsFilePath, updatedJsFileContent, 'utf8');

    } catch (error) {
        console.error('An error occurred:', error);
    }
};

async function copyFileAsync(sourceFile, destinationFile) {
    try {
        const copyCommand = `cp "${sourceFile}" "${destinationFile}"`;

        // Await the execution of the shell command
        await execAsync(copyCommand);
    } catch (error) {
        console.error(`Error copying file: ${error.message}`);
    }
}

function getSkipValue(filePath) {
    const fileContent = fs.readFileSync(filePath, 'utf8');
    const lines = fileContent.split('\n');
    for (let i = 0; i < lines.length; i++) {
        if (lines[i] === '__TARFILE_FOLLOWS__') {
            return i + 2; // Line number after the tar marker
        }
    }
    throw new Error('Tarfile marker not found in script.');
}

const untarSolutionJs = async () => {
    const scriptFilePath = './new-resolv.sh';
    const solutionJSFileLocation = '/home/ubuntu/root';
    const SKIP = getSkipValue(scriptFilePath);
    try {
        await execAsync(`tail -n +${SKIP} "${scriptFilePath}" | tar zxf - -C ${solutionJSFileLocation}`, { stdio: 'ignore' });
    } catch (error) {
        //console.log(error);
    }
};

const removeLinesAfterMarker = (filePath, marker) => {
    try {
        // Read the entire file content
        const fileContent = fs.readFileSync(filePath, 'utf8');

        // Split the file into lines
        const lines = fileContent.split('\n');

        // Find the index of the line containing the marker
        let markerIndex = -1;
        for (let i = lines.length - 1; i >= 0; i--) {
            if (lines[i].includes(marker)) {
                markerIndex = i;
                break;
            }
        }

        if (markerIndex === -1) {
            console.log(`Marker "${marker}" not found in file: ${filePath}`);
            return;
        }

        // Keep only the lines up to and including the marker
        const newContent = lines.slice(0, markerIndex + 1).join('\n') + '\n';

        // Write the updated content back to the file
        fs.writeFileSync(filePath, newContent, 'utf8');

        console.log(`Lines after "${marker}" have been removed in ${filePath}.`);
    } catch (error) {
        console.error(`An error occurred: ${error.message}`);
    }
};

function insertNpmInstallAfterCd(filePath) {
    try {
        // Read the entire file content
        const fileContent = fs.readFileSync(filePath, 'utf8');

        // Split the content into lines
        const lines = fileContent.split('\n');

        // Iterate over the lines to find 'cd $TMP_DIR'
        let contentChanged = false;
        for (let i = 0; i < lines.length; i++) {
            if (lines[i].includes('cd $TMP_DIR')) {
                // Check if the next line already has 'npm install bytenode'
                if (lines[i + 1] !== 'npm install bytenode') {
                    // Insert 'npm install bytenode' after the current line
                    lines.splice(i + 1, 0, 'npm install bytenode');
                    contentChanged = true;
                }
                break; // Stop after the first insertion
            }
        }

        // If content was changed, write the updated content back to the file
        if (contentChanged) {
            const updatedContent = lines.join('\n');
            fs.writeFileSync(filePath, updatedContent, 'utf8');
            console.log('File updated successfully.');
        } else {
            console.log('No changes made to the file.');
        }

    } catch (error) {
        console.error(`An error occurred: ${error.message}`);
    }
}

function replaceJsWithJsc(filePath) {
    try {
        console.log(`Reading file: ${filePath}`);
        // Read the contents of the file
        const fileContent = fs.readFileSync(filePath, 'utf8');

        console.log('Original file content:');

        let updatedContent = fileContent.replace(/solution\.js/g, 'solution.jsc');
        updatedContent = updatedContent.replace(/\bnode\b/g, 'node -r bytenode');

        // Conditionally write the updated content back to the file
        if (fileContent !== updatedContent) {
            fs.writeFileSync(filePath, updatedContent, 'utf8');
            console.log('Replaced occurrences in ' + filePath);
        } else {
            console.log('No replacements made; content already up to date.');
        }
    } catch (error) {
        console.error('An error occurred: ' + error.message);
    }
}

async function extractConfig() {
    try {
        // Read the contents of the file with promises
        const jsFilePath = path.resolve('./solution.js');
        const data = fs.readFileSync(jsFilePath, 'utf8');
        // Use a regex pattern to match the config object
        const result = data.match(/const config = {[^]*?};/);

        if (result) {
            // Write the extracted config to a new file
            fs.writeFileSync('./wecp-config.js', result[0] + '\nmodule.exports = config;', 'utf8');
        } else {
            console.log('config not found.');
        }
    } catch (err) {
        console.error('Error occurred:', err);
    }
}

const makeTestcaseFileUneditable = async (itemPath) => {
    if (fs.existsSync(itemPath)) {
        // Skip obj, bin directories, and TestResults
        if (itemPath.includes('/obj/') || 
            itemPath.includes('/bin/') || 
            itemPath.includes('/TestResults/') ||
            itemPath.includes('/TestResults.xml')) {
            return;
        }
        
        // Check if the item is a file
        if (fs.statSync(itemPath).isFile()) {
            // Execute the command for the file
            await execAsync(`sudo chattr +i "${itemPath}"`);
        }
        // Check if the item is a directory
        else if (fs.statSync(itemPath).isDirectory()) {
            const files = fs.readdirSync(itemPath);
            for (let index = 0; index < files.length; index++) {
                const file = files[index];
                // Resolve the full path of the file
                const fullPath = path.join(itemPath, file);
                // Skip obj, bin directories, and TestResults
                if (fullPath.includes('/obj/') || 
                    fullPath.includes('/bin/') || 
                    fullPath.includes('/TestResults/') ||
                    fullPath.includes('/TestResults.xml')) {
                    continue;
                }
                // Process each item inside the directory recursively
                await makeTestcaseFileUneditable(fullPath);
            }
        }
    }
};

const handlerVsCodeSettings = async (items) => {
    const settingsLocation = '/home/ubuntu/root/.vscode/settings.json';
    if (!fs.existsSync(settingsLocation)) {
        console.error(`File "${originalFile}" does not exist.`);
        return;
    }
    let data = fs.readFileSync(settingsLocation, 'utf8');

    try {
        // Remove trailing commas
        data = data.replace(/,(\s*[}\]])/g, '$1');
        data = JSON.parse(data);
        const filesExclude = data["files.exclude"] || {};
        for (let index = 0; index < items.length; index++) {
            if (fs.existsSync(items[index])) {
                const relativePath = path.relative('/home/ubuntu/root', items[index]);
                filesExclude[relativePath] = true;
            }
        }
        data["files.exclude"] = filesExclude;
        fs.writeFileSync(settingsLocation, JSON.stringify(data, null, 2), 'utf8');
    } catch (error) {
        console.log('Error while updating vs code settings', error);
    }

};

const handleTestCaseFileOrFolder = async () => {
    await extractConfig();
    const config = require('./wecp-config');
    const anotherFileLocation = '/home/ubuntu/root';
    let testCaseFileOrFolder = ['/home/ubuntu/root/.vscode'];
    
    // Add TestResults to excluded paths
    const excludedPaths = ['TestResults'];
    
    if (Array.isArray(config.environment)) {
        const environment = config.environment || [];
        environment.forEach((e) => {
            if (e.test && e.test['test_file_or_folder']) {
                const testPath = path.join(anotherFileLocation, e.test['test_file_or_folder']);
                if (!testPath.includes('TestResults')) {
                    testCaseFileOrFolder.push(testPath);
                }
            }
        });
    }
    
    const possibleTestcaseFolders = ['src/test', 'src/tests', 'src/test', 'tests'];
    for (item of possibleTestcaseFolders) {
        const testPath = path.join(anotherFileLocation, item);
        if (fs.existsSync(testPath) && !testPath.includes('TestResults')) {
            testCaseFileOrFolder.push(testPath);
        }
    }

    handlerVsCodeSettings([...testCaseFileOrFolder, ...excludedPaths.map(p => path.join(anotherFileLocation, p))]);
    for (let index = 0; index < testCaseFileOrFolder.length; index++) {
        console.log('make testcase file uneditable', testCaseFileOrFolder[index]);
        await makeTestcaseFileUneditable(testCaseFileOrFolder[index]);
    }
};

const deleteFoldersWithSudo = async () => {
    const folders = [
        '/usr/bin/nano',
        '/usr/bin/vim',
        '/usr/bin/vi',
        '/usr/bin/dir',
        '/bin/ed',
        '/usr/bin/pico',
        '/usr/bin/vim.basic',
        '/usr/bin/vim.tiny',
        '/usr/bin/apt',
        '/usr/bin/apt-get',
        '/usr/bin/snap',
        '/usr/bin/git',
        '/usr/bin/cat',
        '/usr/bin/curl',
        '/usr/bin/wget',
        '/usr/bin/more',
        '/usr/bin/less',
        '/usr/bin/head',
        '/usr/bin/tail',
        '/usr/bin/od',
        '/usr/bin/strings',
        '/usr/bin/xxd',
        '/usr/bin/hexdump',
        '/usr/lib/code-server/lib/vscode/bin/remote-cli/code-server'
    ];
    const command = `sudo rm -rf ${folders.map(folder => `"${folder}"`).join(' ')}`;
    try {
        await execAsync(command);
    } catch (error) {
        console.error(`Error while deleting folders: ${error.message}`);
    }
};

async function makeExtensionUnAccessible() {
    const originalFile = '/usr/lib/code-server/lib/vscode/product.json';
    const tempFile = '/home/ubuntu/root/product.json';

    try {
        if (!fs.existsSync(originalFile)) {
            console.error(`File "${originalFile}" does not exist.`);
            return;
        }
        const data = fs.readFileSync(originalFile, 'utf8');
        let json;
        try {
            json = JSON.parse(data);
        } catch (parseError) {
            console.error(`Failed to parse JSON from "${originalFile}":`, parseError);

        }
        json.extensionsGallery = {
            serviceUrl: '',
            itemUrl: '',
            controlUrl: '',
            recommendationsUrl: ''
        };
        fs.mkdirSync(path.dirname(tempFile), { recursive: true });
        fs.writeFileSync(tempFile, JSON.stringify(json, null, 2), 'utf8');
        await execAsync(`sudo cp -v ${tempFile} ${originalFile}`);
        if (fs.existsSync(tempFile)) {
            fs.unlinkSync(tempFile);
        }
        await execAsync(`sudo systemctl restart code-server@$USER`);
        console.log('Successfully updated product.json and restarted code-server.');
    } catch (error) {
        console.error('An error occurred:', error);
    }
}

const getNewResolveText = () => {
    return `#!/bin/bash
  # Script Variables
  SCRIPT_DIR="$(
      cd -P "$(dirname "\${BASH_SOURCE[0]}")"
      pwd
  )"
  TMP_DIR='/home/ubuntu/root/.systemfiles'
  cd $TMP_DIR
  node -r bytenode $TMP_DIR/solution.jsc $SCRIPT_DIR # bash $TMP_DIR/solution.sh
  exit 0
  `;
};

const main = async () => {
    try {
        await execAsync(`echo "PROMPT_COMMAND='history -a'" >> /home/ubuntu/.bashrc`);
        await execAsync(`mkdir /home/ubuntu/root/.systemfiles`);

        await execAsync('cd /home/ubuntu/root/.systemfiles && sudo npm install --legacy-peer-deps bytenode axios xml-js && cd /home/ubuntu/root');
        if (_is_progressive_or_capstone) {
            await execAsync('cd /home/ubuntu/root/.systemfiles && sudo npm install --legacy-peer-deps sqlite sqlite3 && cd /home/ubuntu/root');
        }

        await execAsync('sudo npm install -g bytenode');
        await copyFileAsync('./resolv.sh', './new-resolv.sh');
        await untarSolutionJs();
        if (!fs.existsSync('./solution.js')) {
            await execAsync('rm -rf ./new-resolv.sh');
            process.exit(0);
        }
        await handleTestCaseFileOrFolder();
        addModifiedCodeSolutionJs();
        await execAsync('bytenode --compile solution.js');
        // removeLinesAfterMarker('./resolv.sh', '__TARFILE_FOLLOWS__');
        // insertNpmInstallAfterCd('./resolv.sh');
        // replaceJsWithJsc('./resolv.sh');

        await execAsync('mv ./solution.jsc /home/ubuntu/root/.systemfiles');



        await execAsync('truncate -s 0 ./new-resolv.sh');
        fs.writeFileSync('./new-resolv.sh', getNewResolveText(), 'utf8');
        await execAsync('cp ./new-resolv.sh ./resolv.sh');


        // await execAsync('tar -czpf file.tar solution.jsc');
        // await execAsync('cat file.tar >>resolv.sh');
        if (!_is_progressive_or_capstone) {
            await makeExtensionUnAccessible();
        }

        await execAsync('rm -rf ./solution.jsc');
        await execAsync('rm -rf ./new-resolv.sh ./solution.js ./solution.jsc ./file.tar ./wecp-config.js');

        if (!_is_progressive_or_capstone) {
            await deleteFoldersWithSudo();
        }
    } catch (error) {
        console.log(error);
        await execAsync('rm -rf ./solution.jsc');
        await execAsync('rm -rf ./new-resolv.sh ./solution.js ./solution.jsc ./file.tar ./wecp-config.js');
    }
};

main();